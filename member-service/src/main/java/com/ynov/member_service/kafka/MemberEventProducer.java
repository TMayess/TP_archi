package com.ynov.member_service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberEventProducer {

    private static final String MEMBER_DELETED_TOPIC = "member-deleted";
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMemberDeletedEvent(Long memberId) {
        log.info("Publishing member-deleted event for memberId: {}", memberId);
        kafkaTemplate.send(MEMBER_DELETED_TOPIC, memberId.toString());
    }
}