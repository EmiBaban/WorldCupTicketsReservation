package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Stadium;
import com.mobylab.springbackend.exception.NotFoundException;
import com.mobylab.springbackend.repository.StadiumRepository;
import com.mobylab.springbackend.service.dto.StadiumDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class StadiumService {

    @Autowired
    private StadiumRepository stadiumRepository;

    public List<StadiumDto> getAllStadiums() {
        return stadiumRepository.findAll().stream().map(stadium -> {
            StadiumDto dto = new StadiumDto();
            dto.setName(stadium.getName());
            dto.setCapacity((stadium.getCapacity()));
            dto.setDescription(stadium.getDescription());
            return dto;
        }).collect(Collectors.toList());
    }

    public Stadium addStadium(StadiumDto stadiumDto) {
        Stadium stadium = new Stadium();
        stadium.setName(stadiumDto.getName());
        stadium.setCapacity(stadiumDto.getCapacity());
        stadium.setDescription(stadiumDto.getDescription());
        return stadiumRepository.save(stadium);
    }

    public void deleteStadium(UUID id) {
        stadiumRepository.deleteById(id);
    }

    public StadiumDto update(UUID id, StadiumDto dto) {
        Stadium existing = stadiumRepository.findById(id).orElseThrow(() -> new NotFoundException("Stadium not found"));
        existing.setName(dto.getName());
        existing.setCapacity(dto.getCapacity());
        existing.setDescription(dto.getDescription());
        return mapToDto(stadiumRepository.save(existing));
    }

    public StadiumDto getById(UUID id) {
        return stadiumRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new NotFoundException("Stadium not found"));
    }

    private StadiumDto mapToDto(Stadium stadium) {
        StadiumDto dto = new StadiumDto();
        dto.setName(stadium.getName());
        dto.setCapacity(stadium.getCapacity());
        dto.setDescription(stadium.getDescription());
        return dto;
    }

//    private Stadium mapToEntity(StadiumDto dto) {
//        Stadium stadium = new Stadium();
//        stadium.setName(dto.getName());
//        stadium.setCapacity(dto.getCapacity());
//        stadium.setDescription(dto.getDescription());
//        return stadium;
//    }
}
