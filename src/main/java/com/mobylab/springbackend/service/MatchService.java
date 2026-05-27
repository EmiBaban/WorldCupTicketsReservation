package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.*;
import com.mobylab.springbackend.enums.SeatStatus;
import com.mobylab.springbackend.repository.MatchRepository;
import com.mobylab.springbackend.repository.SeatRepository;
import com.mobylab.springbackend.repository.StadiumRepository;
import com.mobylab.springbackend.repository.TeamRepository;
import com.mobylab.springbackend.service.dto.MatchDto;
import com.mobylab.springbackend.service.dto.MatchResponseDto;
import com.mobylab.springbackend.service.dto.StadiumDto;
import com.mobylab.springbackend.service.dto.UserDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private StadiumRepository stadiumRepository;
    @Autowired
    private TeamRepository teamRepository;

    public void createMatch(MatchDto dto) {
        Stadium stadium = stadiumRepository.findById(dto.getStadiumId())
                .orElseThrow(() -> new RuntimeException("Stadium not found"));
        Team homeTeam = teamRepository.findById(dto.getHomeTeamId())
                .orElseThrow(() -> new RuntimeException("Home team not found"));
        Team awayTeam = teamRepository.findById(dto.getAwayTeamId())
                .orElseThrow(() -> new RuntimeException("Away team not found"));

        Match match = new Match();
        match.setStadium(stadium);
        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        match.setReferee(dto.getReferee());
        match.setSeatPrice(dto.getSeatPrice());
        match.setDateTime(dto.getDateTime());

        matchRepository.save(match);
        generateSeatsForMatch(match);
    }

    public List<MatchResponseDto> getAllMatches() {
        List<Match> matches = matchRepository.findAll();
        List<MatchResponseDto> response = new ArrayList<>();

        for (Match match : matches) {
            response.add(new MatchResponseDto(
                    match.getId(),
                    match.getStadium().getName(),
                    match.getHomeTeam().getName(),
                    match.getAwayTeam().getName(),
                    match.getReferee(),
                    match.getSeatPrice(),
                    match.getDateTime()
            ));
        }
        return response;
    }

    public Page<MatchResponseDto> getMatchesPaged(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Match> matchesPage = matchRepository.findAll(pageable);

        return matchesPage.map(match -> new MatchResponseDto(
                match.getId(),
                match.getStadium().getName(),
                match.getHomeTeam().getName(),
                match.getAwayTeam().getName(),
                match.getReferee(),
                match.getSeatPrice(),
                match.getDateTime()
        ));
    }

    public Page<StadiumDto> getStadiumsPaged(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
//        Specification<Match> spec = MatchSpecifications.hasSearchTerm(search);

        return stadiumRepository.findAll(pageable).map(stadium -> {
            StadiumDto dto = new StadiumDto();
            dto.setName(stadium.getName());
            dto.setCapacity(stadium.getCapacity());
            dto.setDescription(stadium.getDescription());
            dto.setImageUrl(stadium.getImageUrl());
            dto.setCountry(stadium.getCountry());
            dto.setCity(stadium.getCity());
            return dto;
        });
    }

    private void generateSeatsForMatch(Match match) {
        int totalSeats = match.getStadium().getCapacity();
        List<Seat> seats = new ArrayList<>();
        int seatsPerRow = 10;
        for (int i = 0; i < totalSeats; i++) {
            Seat seat = new Seat();
            seat.setMatch(match);
            seat.setRow(String.valueOf((char) ('A' + (i / seatsPerRow))));
            seat.setNumber((i % seatsPerRow) + 1);
            seat.setStatus(SeatStatus.LIBER);
            seats.add(seat);
        }
        seatRepository.saveAll(seats);
    }
}
