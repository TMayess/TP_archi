package com.ynov.reservation_service.kafka;


import com.ynov.reservation_service.entity.Reservation;
import com.ynov.reservation_service.entity.ReservationStatus;
import com.ynov.reservation_service.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationEventConsumer {

    private final ReservationRepository reservationRepository;

    @KafkaListener(topics = "room-deleted", groupId = "reservation-group")
    public void handleRoomDeleted(String roomId) {
        log.info("Received room-deleted event for roomId: {}", roomId);
        var reservations = reservationRepository
                .findByRoomIdAndStatus(Long.parseLong(roomId), ReservationStatus.CONFIRMED);
        reservations.forEach(r -> {
            r.setStatus(ReservationStatus.CANCELLED);
            reservationRepository.save(r);
        });
        log.info("Cancelled {} reservations for roomId: {}", reservations.size(), roomId);
    }


    @KafkaListener(topics = "member-deleted", groupId = "reservation-group")
    public void handleMemberDeleted(String memberId) {
        log.info("Received member-deleted event for memberId: {}", memberId);
        var reservations = reservationRepository
                .findByMemberId(Long.parseLong(memberId));
        reservationRepository.deleteAll(reservations);
        log.info("Deleted {} reservations for memberId: {}", reservations.size(), memberId);
    }
}