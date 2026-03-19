package com.ynov.member_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String email;

    @Enumerated(EnumType.STRING)
    private SubscriptionType subscriptionType;

    private boolean suspended;
    private Integer maxConcurrentBookings;
}