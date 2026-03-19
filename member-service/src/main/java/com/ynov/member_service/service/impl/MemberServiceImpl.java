package com.ynov.member_service.service.impl;


import com.ynov.member_service.dto.MemberRequest;
import com.ynov.member_service.dto.MemberResponse;
import com.ynov.member_service.entity.Member;
import com.ynov.member_service.entity.SubscriptionType;
import com.ynov.member_service.kafka.MemberEventProducer;
import com.ynov.member_service.repository.MemberRepository;
import com.ynov.member_service.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberEventProducer memberEventProducer;

    @Override
    public MemberResponse createMember(MemberRequest request) {
        Member member = Member.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .subscriptionType(request.getSubscriptionType())
                .suspended(false)
                .maxConcurrentBookings(resolveMaxBookings(request.getSubscriptionType()))
                .build();
        return toResponse(memberRepository.save(member));
    }

    @Override
    public MemberResponse getMemberById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll()
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MemberResponse updateMember(Long id, MemberRequest request) {
        Member member = findById(id);
        member.setFullName(request.getFullName());
        member.setEmail(request.getEmail());
        member.setSubscriptionType(request.getSubscriptionType());
        member.setMaxConcurrentBookings(resolveMaxBookings(request.getSubscriptionType()));
        return toResponse(memberRepository.save(member));
    }

    @Override
    public void deleteMember(Long id) {
        Member member = findById(id);
        memberEventProducer.sendMemberDeletedEvent(id);
        memberRepository.delete(member);
    }

    @Override
    public void updateSuspension(Long id, boolean suspended) {
        Member member = findById(id);
        member.setSuspended(suspended);
        memberRepository.save(member);
    }

    // --- Helpers ---

    private int resolveMaxBookings(SubscriptionType type) {
        return switch (type) {
            case BASIC -> 2;
            case PRO -> 5;
            case ENTERPRISE -> 10;
        };
    }

    private Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found: " + id));
    }

    private MemberResponse toResponse(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .fullName(member.getFullName())
                .email(member.getEmail())
                .subscriptionType(member.getSubscriptionType())
                .suspended(member.isSuspended())
                .maxConcurrentBookings(member.getMaxConcurrentBookings())
                .build();
    }
}