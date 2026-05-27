package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Stadium;
import com.mobylab.springbackend.exception.NotFoundException;
import com.mobylab.springbackend.repository.StadiumRepository;
import com.mobylab.springbackend.service.dto.MatchDto;
import com.mobylab.springbackend.service.dto.MatchResponseDto;
import com.mobylab.springbackend.service.dto.StadiumDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
            dto.setCountry(stadium.getCountry());
            dto.setCity(stadium.getCity());
            dto.setImageUrl(stadium.getImageUrl());
            return dto;
        }).collect(Collectors.toList());
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


    public Stadium addStadium(StadiumDto stadiumDto) {
        Stadium stadium = new Stadium();
        stadium.setName(stadiumDto.getName());
        stadium.setCapacity(stadiumDto.getCapacity());
        stadium.setDescription(stadiumDto.getDescription());
        stadium.setImageUrl(stadiumDto.getImageUrl());
        stadium.setCountry(stadiumDto.getCountry());
        stadium.setCity(stadiumDto.getCity());
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
        existing.setImageUrl(dto.getImageUrl());
        existing.setCountry(dto.getCountry());
        existing.setCity(dto.getCity());
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

}
