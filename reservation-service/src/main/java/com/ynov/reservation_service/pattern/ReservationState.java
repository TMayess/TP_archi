package com.ynov.reservation_service.pattern;


public interface ReservationState {
    void cancel();
    void complete();
    String getStatus();
}