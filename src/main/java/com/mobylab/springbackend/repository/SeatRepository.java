package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.Seat;
import com.mobylab.springbackend.entity.Stadium;
import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.enums.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeatRepository extends JpaRepository<Seat, UUID> {
    List<Seat> findAllByMatchId(UUID matchId);
    List<Seat> findAllByStatusAndSelectedAtBefore(SeatStatus status, LocalDateTime before);
    List<Seat> findAllByStatus(SeatStatus status);
}
