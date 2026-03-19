package com.ynov.member_service.dto;

import com.ynov.member_service.entity.SubscriptionType;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MemberRequest {
    private String fullName;
    private String email;
    private SubscriptionType subscriptionType;
}