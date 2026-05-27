package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Match;
import com.mobylab.springbackend.entity.Seat;
import com.mobylab.springbackend.entity.Stadium;
import com.mobylab.springbackend.entity.Team;
import com.mobylab.springbackend.enums.SeatStatus;
import com.mobylab.springbackend.repository.MatchRepository;
import com.mobylab.springbackend.repository.SeatRepository;
import com.mobylab.springbackend.repository.StadiumRepository;
import com.mobylab.springbackend.repository.TeamRepository;
import com.mobylab.springbackend.service.dto.MatchDto;
import com.mobylab.springbackend.service.dto.MatchResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private StadiumRepository stadiumRepository;
    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private MatchService matchService;

    @Test
    void createMatchShouldThrowWhenStadiumDoesNotExist() {
        UUID stadiumId = UUID.randomUUID();
        MatchDto dto = new MatchDto(stadiumId, UUID.randomUUID(), UUID.randomUUID(), "Ref", 40.0, LocalDateTime.now());

        when(stadiumRepository.findById(stadiumId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> matchService.createMatch(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Stadium not found");

        verify(matchRepository, never()).save(any());
    }

    @Test
    void createMatchShouldSaveMatchAndGenerateSeats() {
        UUID stadiumId = UUID.randomUUID();
        UUID homeTeamId = UUID.randomUUID();
        UUID awayTeamId = UUID.randomUUID();
        LocalDateTime dateTime = LocalDateTime.of(2026, 6, 1, 20, 30);
        Stadium stadium = new Stadium();
        stadium.setId(stadiumId);
        stadium.setName("Arena");
        stadium.setCapacity(12);
        Team homeTeam = new Team();
        homeTeam.setId(homeTeamId);
        homeTeam.setName("Home");
        Team awayTeam = new Team();
        awayTeam.setId(awayTeamId);
        awayTeam.setName("Away");
        MatchDto dto = new MatchDto(stadiumId, homeTeamId, awayTeamId, "Ref", 55.0, dateTime);

        when(stadiumRepository.findById(stadiumId)).thenReturn(Optional.of(stadium));
        when(teamRepository.findById(homeTeamId)).thenReturn(Optional.of(homeTeam));
        when(teamRepository.findById(awayTeamId)).thenReturn(Optional.of(awayTeam));

        matchService.createMatch(dto);

        ArgumentCaptor<Match> matchCaptor = ArgumentCaptor.forClass(Match.class);
        verify(matchRepository).save(matchCaptor.capture());
        assertThat(matchCaptor.getValue().getStadium()).isSameAs(stadium);
        assertThat(matchCaptor.getValue().getHomeTeam()).isSameAs(homeTeam);
        assertThat(matchCaptor.getValue().getAwayTeam()).isSameAs(awayTeam);
        assertThat(matchCaptor.getValue().getReferee()).isEqualTo("Ref");
        assertThat(matchCaptor.getValue().getSeatPrice()).isEqualTo(55.0);
        assertThat(matchCaptor.getValue().getDateTime()).isEqualTo(dateTime);

        ArgumentCaptor<List<Seat>> seatsCaptor = ArgumentCaptor.forClass(List.class);
        verify(seatRepository).saveAll(seatsCaptor.capture());
        List<Seat> seats = seatsCaptor.getValue();
        assertThat(seats).hasSize(12);
        assertThat(seats.get(0).getRow()).isEqualTo("A");
        assertThat(seats.get(0).getNumber()).isEqualTo(1);
        assertThat(seats.get(9).getRow()).isEqualTo("A");
        assertThat(seats.get(9).getNumber()).isEqualTo(10);
        assertThat(seats.get(10).getRow()).isEqualTo("B");
        assertThat(seats.get(10).getNumber()).isEqualTo(1);
        assertThat(seats).allSatisfy(seat -> {
            assertThat(seat.getMatch()).isSameAs(matchCaptor.getValue());
            assertThat(seat.getStatus()).isEqualTo(SeatStatus.LIBER);
        });
    }

    @Test
    void getAllMatchesShouldMapMatchesToResponseDtos() {
        UUID matchId = UUID.randomUUID();
        LocalDateTime dateTime = LocalDateTime.of(2026, 6, 1, 20, 30);
        Match match = new Match();
        match.setId(matchId);
        match.setStadium(stadium("Arena", 10));
        match.setHomeTeam(team("Home"));
        match.setAwayTeam(team("Away"));
        match.setReferee("Ref");
        match.setSeatPrice(60.0);
        match.setDateTime(dateTime);

        when(matchRepository.findAll()).thenReturn(List.of(match));

        List<MatchResponseDto> result = matchService.getAllMatches();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(matchId);
        assertThat(result.get(0).getStadiumName()).isEqualTo("Arena");
        assertThat(result.get(0).getHomeTeamName()).isEqualTo("Home");
        assertThat(result.get(0).getAwayTeamName()).isEqualTo("Away");
        assertThat(result.get(0).getReferee()).isEqualTo("Ref");
        assertThat(result.get(0).getSeatPrice()).isEqualTo(60.0);
        assertThat(result.get(0).getDateTime()).isEqualTo(dateTime);
    }

    private static Stadium stadium(String name, int capacity) {
        Stadium stadium = new Stadium();
        stadium.setName(name);
        stadium.setCapacity(capacity);
        return stadium;
    }

    private static Team team(String name) {
        Team team = new Team();
        team.setName(name);
        return team;
    }
}
