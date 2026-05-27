package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Stadium;
import com.mobylab.springbackend.exception.NotFoundException;
import com.mobylab.springbackend.repository.StadiumRepository;
import com.mobylab.springbackend.service.dto.StadiumDto;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StadiumServiceTest {

    @Mock
    private StadiumRepository stadiumRepository;

    @InjectMocks
    private StadiumService stadiumService;

    @Test
    void getAllStadiumsShouldMapEntitiesToDtos() {
        Stadium stadium = stadium("Arena", 50000, "Romania", "Bucharest", "National arena", "arena.jpg");

        when(stadiumRepository.findAll()).thenReturn(List.of(stadium));

        List<StadiumDto> result = stadiumService.getAllStadiums();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Arena");
        assertThat(result.get(0).getCapacity()).isEqualTo(50000);
        assertThat(result.get(0).getCountry()).isEqualTo("Romania");
        assertThat(result.get(0).getCity()).isEqualTo("Bucharest");
        assertThat(result.get(0).getDescription()).isEqualTo("National arena");
        assertThat(result.get(0).getImageUrl()).isEqualTo("arena.jpg");
    }

    @Test
    void addStadiumShouldSaveEntityFromDto() {
        StadiumDto dto = new StadiumDto("Arena", 50000, "National arena", "Romania", "Bucharest", "arena.jpg");
        when(stadiumRepository.save(org.mockito.Mockito.any(Stadium.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Stadium result = stadiumService.addStadium(dto);

        assertThat(result.getName()).isEqualTo("Arena");
        assertThat(result.getCapacity()).isEqualTo(50000);
        assertThat(result.getCountry()).isEqualTo("Romania");
        assertThat(result.getCity()).isEqualTo("Bucharest");
        assertThat(result.getDescription()).isEqualTo("National arena");
        assertThat(result.getImageUrl()).isEqualTo("arena.jpg");
    }

    @Test
    void updateShouldThrowNotFoundWhenStadiumMissing() {
        UUID id = UUID.randomUUID();
        when(stadiumRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> stadiumService.update(id, new StadiumDto()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Stadium not found");
    }

    @Test
    void updateShouldModifyExistingStadium() {
        UUID id = UUID.randomUUID();
        Stadium existing = stadium("Old", 100, "Old country", "Old city", "Old description", "old.jpg");
        StadiumDto dto = new StadiumDto("New", 200, "New description", "New country", "New city", "new.jpg");

        when(stadiumRepository.findById(id)).thenReturn(Optional.of(existing));
        when(stadiumRepository.save(existing)).thenReturn(existing);

        StadiumDto result = stadiumService.update(id, dto);

        ArgumentCaptor<Stadium> stadiumCaptor = ArgumentCaptor.forClass(Stadium.class);
        verify(stadiumRepository).save(stadiumCaptor.capture());
        assertThat(stadiumCaptor.getValue().getName()).isEqualTo("New");
        assertThat(stadiumCaptor.getValue().getCapacity()).isEqualTo(200);
        assertThat(stadiumCaptor.getValue().getCountry()).isEqualTo("New country");
        assertThat(stadiumCaptor.getValue().getCity()).isEqualTo("New city");
        assertThat(stadiumCaptor.getValue().getDescription()).isEqualTo("New description");
        assertThat(stadiumCaptor.getValue().getImageUrl()).isEqualTo("new.jpg");
        assertThat(result.getName()).isEqualTo("New");
        assertThat(result.getCapacity()).isEqualTo(200);
        assertThat(result.getDescription()).isEqualTo("New description");
    }

    @Test
    void getByIdShouldThrowNotFoundWhenMissing() {
        UUID id = UUID.randomUUID();
        when(stadiumRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> stadiumService.getById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Stadium not found");
    }

    private static Stadium stadium(String name, int capacity, String country, String city, String description, String imageUrl) {
        Stadium stadium = new Stadium();
        stadium.setName(name);
        stadium.setCapacity(capacity);
        stadium.setCountry(country);
        stadium.setCity(city);
        stadium.setDescription(description);
        stadium.setImageUrl(imageUrl);
        return stadium;
    }
}
