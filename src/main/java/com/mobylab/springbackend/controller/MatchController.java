package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.service.MatchService;
import com.mobylab.springbackend.service.SeatService;
import com.mobylab.springbackend.service.StadiumService;
import com.mobylab.springbackend.service.dto.MatchDto;
import com.mobylab.springbackend.service.dto.MatchResponseDto;
import com.mobylab.springbackend.service.dto.SeatDto;
import com.mobylab.springbackend.service.dto.SelectSeatRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/match")
public class MatchController implements SecuredRestController {
    @Autowired
    private MatchService matchService;

    @Autowired
    private SeatService seatService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public ResponseEntity<?> createMatch(@RequestBody MatchDto dto) {
        matchService.createMatch(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Match created successfully.");
    }

    @GetMapping("/{matchId}/seats")
    public ResponseEntity<List<SeatDto>> getSeatsForMatch(@PathVariable UUID matchId) {
        List<SeatDto> seats = seatService.getSeatsByMatchId(matchId);
        return ResponseEntity.ok(seats);
    }

    @PutMapping("/{matchId}/select-seat")
    public ResponseEntity<?> selectSeat(@PathVariable UUID matchId, @RequestBody SelectSeatRequest request) {
        seatService.selectSeat(matchId, request.getSeatId());
        return ResponseEntity.ok("Locul a fost selectat temporar.");
    }

    @GetMapping("/getAllMatches")
    public ResponseEntity<List<MatchResponseDto>> getAllMatches() {
        return ResponseEntity.ok(matchService.getAllMatches());
    }
}
