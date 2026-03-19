package com.ynov.member_service.kafka;


import com.ynov.member_service.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberEventConsumer {

    private final MemberService memberService;

    @KafkaListener(topics = "member-suspend", groupId = "member-group")
    public void handleSuspendMember(String memberId) {
        log.info("Received member-suspend event for memberId: {}", memberId);
        memberService.updateSuspension(Long.parseLong(memberId), true);
    }

    @KafkaListener(topics = "member-unsuspend", groupId = "member-group")
    public void handleUnsuspendMember(String memberId) {
        log.info("Received member-unsuspend event for memberId: {}", memberId);
        memberService.updateSuspension(Long.parseLong(memberId), false);
    }
}