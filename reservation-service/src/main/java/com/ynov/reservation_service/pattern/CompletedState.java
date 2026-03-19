package com.ynov.reservation_service.pattern;


public class CompletedState implements ReservationState {

    @Override
    public void cancel() {
        throw new IllegalStateException("Cannot cancel a COMPLETED reservation");
    }

    @Override
    public void complete() {
        throw new IllegalStateException("Reservation is already COMPLETED");
    }

    @Override
    public String getStatus() {
        return "COMPLETED";
    }
}