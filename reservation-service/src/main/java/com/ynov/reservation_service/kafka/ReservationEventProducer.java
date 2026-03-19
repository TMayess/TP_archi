package com.ynov.reservation_service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReservationEventProducer {

    private static final String MEMBER_SUSPEND_TOPIC = "member-suspend";
    private static final String MEMBER_UNSUSPEND_TOPIC = "member-unsuspend";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendSuspendMemberEvent(Long memberId) {
        log.info("Publishing member-suspend event for memberId: {}", memberId);
        kafkaTemplate.send(MEMBER_SUSPEND_TOPIC, memberId.toString());
    }

    public void sendUnsuspendMemberEvent(Long memberId) {
        log.info("Publishing member-unsuspend event for memberId: {}", memberId);
        kafkaTemplate.send(MEMBER_UNSUSPEND_TOPIC, memberId.toString());
    }
}