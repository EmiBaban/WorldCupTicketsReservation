package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Seat;
import com.mobylab.springbackend.enums.SeatStatus;
import com.mobylab.springbackend.repository.SeatRepository;
import com.mobylab.springbackend.service.dto.SeatDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private SeatService seatService;

    @Test
    void getSeatsByMatchIdShouldMapSeatsToDtos() {
        UUID matchId = UUID.randomUUID();
        UUID seatId = UUID.randomUUID();
        Seat seat = new Seat();
        seat.setId(seatId);
        seat.setRow("A");
        seat.setNumber(7);
        seat.setStatus(SeatStatus.LIBER);

        when(seatRepository.findAllByMatchId(matchId)).thenReturn(List.of(seat));

        List<SeatDto> result = seatService.getSeatsByMatchId(matchId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(seatId);
        assertThat(result.get(0).getRow()).isEqualTo("A");
        assertThat(result.get(0).getNumber()).isEqualTo(7);
        assertThat(result.get(0).getStatus()).isEqualTo("LIBER");
    }

    @Test
    void selectSeatShouldThrowWhenSeatDoesNotExist() {
        UUID seatId = UUID.randomUUID();
        when(seatRepository.findById(seatId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> seatService.selectSeat(UUID.randomUUID(), seatId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Seat not found");

        verify(seatRepository, never()).save(org.mockito.Mockito.any());
    }

    @Test
    void selectSeatShouldThrowWhenSeatIsNotAvailable() {
        UUID seatId = UUID.randomUUID();
        Seat seat = new Seat();
        seat.setStatus(SeatStatus.OCUPAT);

        when(seatRepository.findById(seatId)).thenReturn(Optional.of(seat));

        assertThatThrownBy(() -> seatService.selectSeat(UUID.randomUUID(), seatId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Seat is not available");

        verify(seatRepository, never()).save(org.mockito.Mockito.any());
    }

    @Test
    void selectSeatShouldMarkSeatAsSelectedAndSetSelectedAt() {
        UUID seatId = UUID.randomUUID();
        Seat seat = new Seat();
        seat.setStatus(SeatStatus.LIBER);

        when(seatRepository.findById(seatId)).thenReturn(Optional.of(seat));

        seatService.selectSeat(UUID.randomUUID(), seatId);

        ArgumentCaptor<Seat> seatCaptor = ArgumentCaptor.forClass(Seat.class);
        verify(seatRepository).save(seatCaptor.capture());

        assertThat(seatCaptor.getValue().getStatus()).isEqualTo(SeatStatus.SELECTAT);
        assertThat(seatCaptor.getValue().getSelectedAt()).isNotNull();
    }
}
