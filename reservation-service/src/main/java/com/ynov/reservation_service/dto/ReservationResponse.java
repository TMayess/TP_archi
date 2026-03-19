package com.ynov.reservation_service.dto;


import com.ynov.reservation_service.entity.ReservationStatus;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ReservationResponse {
    private Long id;
    private Long roomId;
    private Long memberId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private ReservationStatus status;
}