package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Seat;
import com.mobylab.springbackend.enums.SeatStatus;
import com.mobylab.springbackend.repository.SeatRepository;
import com.mobylab.springbackend.service.dto.SeatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeatService {
    @Autowired
    private SeatRepository seatRepository;

    public List<SeatDto> getSeatsByMatchId(UUID matchId) {
        List<Seat> seats = seatRepository.findAllByMatchId(matchId);
        return seats.stream()
                .map(seat -> new SeatDto(
                        seat.getId(),
                        seat.getRow(),
                        seat.getNumber(),
                        seat.getStatus().name()
                )).toList();
    }

    public void selectSeat(UUID matchId, UUID seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        if (seat.getStatus() != SeatStatus.LIBER) {
            throw new RuntimeException("Seat is not available");
        }

        seat.setStatus(SeatStatus.SELECTAT);
        seat.setSelectedAt(LocalDateTime.now());
        seatRepository.save(seat);
    }
}

