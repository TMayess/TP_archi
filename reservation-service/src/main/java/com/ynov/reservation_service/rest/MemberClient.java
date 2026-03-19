package com.ynov.reservation_service.rest;


import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class MemberClient {

    private final RestTemplate restTemplate;
    private static final String MEMBER_SERVICE_URL = "http://member-service/members";

    public boolean isMemberSuspended(Long memberId) {
        try {
            var response = restTemplate.getForObject(
                    MEMBER_SERVICE_URL + "/" + memberId,
                    java.util.Map.class
            );
            return response != null && Boolean.TRUE.equals(response.get("suspended"));
        } catch (Exception e) {
            throw new RuntimeException("Member not found or Member Service unavailable");
        }
    }

    public void updateSuspension(Long memberId, boolean suspended) {
        restTemplate.patchForObject(
                MEMBER_SERVICE_URL + "/" + memberId + "/suspension?suspended=" + suspended,
                null,
                Void.class
        );
    }

    public int getMaxConcurrentBookings(Long memberId) {
        var response = restTemplate.getForObject(
                MEMBER_SERVICE_URL + "/" + memberId,
                java.util.Map.class
        );
        return response != null ? (Integer) response.get("maxConcurrentBookings") : 0;
    }
}