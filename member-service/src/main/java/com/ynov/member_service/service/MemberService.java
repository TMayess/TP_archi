package com.ynov.member_service.service;

import com.ynov.member_service.dto.MemberRequest;
import com.ynov.member_service.dto.MemberResponse;
import java.util.List;

public interface MemberService {
    MemberResponse createMember(MemberRequest request);
    MemberResponse getMemberById(Long id);
    List<MemberResponse> getAllMembers();
    MemberResponse updateMember(Long id, MemberRequest request);
    void deleteMember(Long id);
    void updateSuspension(Long id, boolean suspended);
}