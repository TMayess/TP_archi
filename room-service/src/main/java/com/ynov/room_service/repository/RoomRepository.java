package com.ynov.room_service.repository;

import com.ynov.room_service.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByCity(String city);
    List<Room> findByAvailableTrue();
}