package com.ynov.room_service.service;

import com.ynov.room_service.dto.RoomRequest;
import com.ynov.room_service.dto.RoomResponse;
import java.util.List;



public interface RoomService {
    RoomResponse createRoom(RoomRequest request);
    RoomResponse getRoomById(Long id);
    List<RoomResponse> getAllRooms();
    List<RoomResponse> getAvailableRooms();
    List<RoomResponse> getRoomsByCity(String city);
    RoomResponse updateRoom(Long id, RoomRequest request);
    void deleteRoom(Long id);
    void setRoomAvailability(Long id, boolean available);
}