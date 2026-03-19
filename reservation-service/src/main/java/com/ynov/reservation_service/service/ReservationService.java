package com.ynov.reservation_service.service;



import com.ynov.reservation_service.dto.ReservationRequest;
import com.ynov.reservation_service.dto.ReservationResponse;
import java.util.List;

public interface ReservationService {
    ReservationResponse createReservation(ReservationRequest request);
    ReservationResponse getReservationById(Long id);
    List<ReservationResponse> getAllReservations();
    ReservationResponse cancelReservation(Long id);
    ReservationResponse completeReservation(Long id);
}