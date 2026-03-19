package com.ynov.reservation_service.pattern;


public class ConfirmedState implements ReservationState {

    @Override
    public void cancel() {
        System.out.println("Reservation cancelled from CONFIRMED state");
    }

    @Override
    public void complete() {
        System.out.println("Reservation completed from CONFIRMED state");
    }

    @Override
    public String getStatus() {
        return "CONFIRMED";
    }
}