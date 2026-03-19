package com.ynov.room_service.dto;


import com.ynov.room_service.entity.RoomType;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomRequest {
    private String name;
    private String city;
    private Integer capacity;
    private RoomType type;
    private BigDecimal hourlyRate;
}