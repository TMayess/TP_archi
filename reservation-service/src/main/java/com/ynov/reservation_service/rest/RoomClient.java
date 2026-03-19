package com.ynov.reservation_service.rest;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class RoomClient {

    private final RestTemplate restTemplate;
    private static final String ROOM_SERVICE_URL = "http://room-service/rooms";
    public boolean isRoomAvailable(Long roomId) {
        try {
            var response = restTemplate.getForObject(
                    ROOM_SERVICE_URL + "/" + roomId,
                    java.util.Map.class
            );
            return response != null && Boolean.TRUE.equals(response.get("available"));
        } catch (Exception e) {
            throw new RuntimeException("Room not found or Room Service unavailable");
        }
    }

    public void setRoomAvailability(Long roomId, boolean available) {
        restTemplate.patchForObject(
                ROOM_SERVICE_URL + "/" + roomId + "/availability?available=" + available,
                null,
                Void.class
        );
    }
}