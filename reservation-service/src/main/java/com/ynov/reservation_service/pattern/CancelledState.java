package com.ynov.reservation_service.pattern;


public class CancelledState implements ReservationState {

    @Override
    public void cancel() {
        throw new IllegalStateException("Reservation is already CANCELLED");
    }

    @Override
    public void complete() {
        throw new IllegalStateException("Cannot complete a CANCELLED reservation");
    }

    @Override
    public String getStatus() {
        return "CANCELLED";
    }
}