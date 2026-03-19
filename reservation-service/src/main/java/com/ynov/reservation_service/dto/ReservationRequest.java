package com.ynov.reservation_service.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ReservationRequest {
    private Long roomId;
    private Long memberId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}