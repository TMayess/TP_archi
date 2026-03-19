package com.ynov.reservation_service.repository;


import com.ynov.reservation_service.entity.Reservation;
import com.ynov.reservation_service.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByMemberIdAndStatus(Long memberId, ReservationStatus status);

    List<Reservation> findByRoomIdAndStatus(Long roomId, ReservationStatus status);

    List<Reservation> findByMemberId(Long memberId);
    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.roomId = :roomId " +
            "AND r.status = 'CONFIRMED' " +
            "AND r.startDateTime < :endDateTime " +
            "AND r.endDateTime > :startDateTime")
    boolean existsOverlappingReservation(
            @Param("roomId") Long roomId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );
}