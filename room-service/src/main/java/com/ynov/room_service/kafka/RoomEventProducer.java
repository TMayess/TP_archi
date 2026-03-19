package com.ynov.room_service.kafka;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoomEventProducer {

    private static final String ROOM_DELETED_TOPIC = "room-deleted";
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendRoomDeletedEvent(Long roomId) {
        log.info("Publishing room-deleted event for roomId: {}", roomId);
        kafkaTemplate.send(ROOM_DELETED_TOPIC, roomId.toString());
    }
}