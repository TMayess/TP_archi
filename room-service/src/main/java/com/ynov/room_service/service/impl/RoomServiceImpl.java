package com.ynov.room_service.service.impl;


import com.ynov.room_service.dto.RoomRequest;
import com.ynov.room_service.dto.RoomResponse;
import com.ynov.room_service.entity.Room;
import com.ynov.room_service.kafka.RoomEventProducer;
import com.ynov.room_service.repository.RoomRepository;
import com.ynov.room_service.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomEventProducer roomEventProducer;

    @Override
    public RoomResponse createRoom(RoomRequest request) {
        Room room = Room.builder()
                .name(request.getName())
                .city(request.getCity())
                .capacity(request.getCapacity())
                .type(request.getType())
                .hourlyRate(request.getHourlyRate())
                .available(true)
                .build();
        return toResponse(roomRepository.save(room));
    }

    @Override
    public RoomResponse getRoomById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll()
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponse> getAvailableRooms() {
        return roomRepository.findByAvailableTrue()
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponse> getRoomsByCity(String city) {
        return roomRepository.findByCity(city)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RoomResponse updateRoom(Long id, RoomRequest request) {
        Room room = findById(id);
        room.setName(request.getName());
        room.setCity(request.getCity());
        room.setCapacity(request.getCapacity());
        room.setType(request.getType());
        room.setHourlyRate(request.getHourlyRate());
        return toResponse(roomRepository.save(room));
    }

    @Override
    public void deleteRoom(Long id) {
        Room room = findById(id);
        roomEventProducer.sendRoomDeletedEvent(id);
        roomRepository.delete(room);
    }

    @Override
    public void setRoomAvailability(Long id, boolean available) {
        Room room = findById(id);
        room.setAvailable(available);
        roomRepository.save(room);
    }

    private Room findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found: " + id));
    }

    private RoomResponse toResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .city(room.getCity())
                .capacity(room.getCapacity())
                .type(room.getType())
                .hourlyRate(room.getHourlyRate())
                .available(room.isAvailable())
                .build();
    }
}