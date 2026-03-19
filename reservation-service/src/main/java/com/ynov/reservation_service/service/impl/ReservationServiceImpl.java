package com.ynov.reservation_service.service.impl;


import com.ynov.reservation_service.dto.ReservationRequest;
import com.ynov.reservation_service.dto.ReservationResponse;
import com.ynov.reservation_service.entity.Reservation;
import com.ynov.reservation_service.entity.ReservationStatus;
import com.ynov.reservation_service.repository.ReservationRepository;
import com.ynov.reservation_service.rest.MemberClient;
import com.ynov.reservation_service.rest.RoomClient;
import com.ynov.reservation_service.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomClient roomClient;
    private final MemberClient memberClient;

    @Override
    public ReservationResponse createReservation(ReservationRequest request) {
        // 1. Vérifier que la salle est disponible
        if (!roomClient.isRoomAvailable(request.getRoomId())) {
            throw new RuntimeException("Room is not available");
        }

        // 2. Vérifier qu'il n'y a pas de chevauchement de créneau
        if (reservationRepository.existsOverlappingReservation(
                request.getRoomId(), request.getStartDateTime(), request.getEndDateTime())) {
            throw new RuntimeException("Room already booked on this time slot");
        }

        // 3. Vérifier que le membre n'est pas suspendu
        if (memberClient.isMemberSuspended(request.getMemberId())) {
            throw new RuntimeException("Member is suspended");
        }

        // 4. Créer la réservation
        Reservation reservation = Reservation.builder()
                .roomId(request.getRoomId())
                .memberId(request.getMemberId())
                .startDateTime(request.getStartDateTime())
                .endDateTime(request.getEndDateTime())
                .status(ReservationStatus.CONFIRMED)
                .build();
        reservation = reservationRepository.save(reservation);

        // 5. Marquer la salle comme indisponible
        roomClient.setRoomAvailability(request.getRoomId(), false);

        // 6. Vérifier le quota du membre et suspendre si nécessaire
        long activeCount = reservationRepository
                .findByMemberIdAndStatus(request.getMemberId(), ReservationStatus.CONFIRMED)
                .size();
        int maxBookings = memberClient.getMaxConcurrentBookings(request.getMemberId());
        if (activeCount >= maxBookings) {
            memberClient.updateSuspension(request.getMemberId(), true);
        }

        return toResponse(reservation);
    }

    @Override
    public ReservationResponse cancelReservation(Long id) {
        Reservation reservation = findById(id);
        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new RuntimeException("Only CONFIRMED reservations can be cancelled");
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        // Libérer la salle
        roomClient.setRoomAvailability(reservation.getRoomId(), true);

        // Désuspendre le membre si besoin
        checkAndUnsuspendMember(reservation.getMemberId());

        return toResponse(reservation);
    }

    @Override
    public ReservationResponse completeReservation(Long id) {
        Reservation reservation = findById(id);
        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new RuntimeException("Only CONFIRMED reservations can be completed");
        }
        reservation.setStatus(ReservationStatus.COMPLETED);
        reservationRepository.save(reservation);

        // Libérer la salle
        roomClient.setRoomAvailability(reservation.getRoomId(), true);

        // Désuspendre le membre si besoin
        checkAndUnsuspendMember(reservation.getMemberId());

        return toResponse(reservation);
    }

    @Override
    public ReservationResponse getReservationById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll()
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }


    private void checkAndUnsuspendMember(Long memberId) {
        long activeCount = reservationRepository
                .findByMemberIdAndStatus(memberId, ReservationStatus.CONFIRMED)
                .size();
        int maxBookings = memberClient.getMaxConcurrentBookings(memberId);
        if (activeCount < maxBookings) {
            memberClient.updateSuspension(memberId, false);
        }
    }

    private Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found: " + id));
    }

    private ReservationResponse toResponse(Reservation r) {
        return ReservationResponse.builder()
                .id(r.getId())
                .roomId(r.getRoomId())
                .memberId(r.getMemberId())
                .startDateTime(r.getStartDateTime())
                .endDateTime(r.getEndDateTime())
                .status(r.getStatus())
                .build();
    }
}