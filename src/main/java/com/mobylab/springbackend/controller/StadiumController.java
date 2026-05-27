package com.mobylab.springbackend.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobylab.springbackend.entity.Stadium;
import com.mobylab.springbackend.service.StadiumService;
import com.mobylab.springbackend.service.dto.StadiumDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.io.IOException;
import java.nio.file.*;


@RestController
@RequestMapping("/api/v1/stadiums")
public class StadiumController implements SecuredRestController {
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private StadiumService stadiumService;

    @GetMapping("/getAllStadiums")
    public List<StadiumDto> getAllStadiums() {
        return stadiumService.getAllStadiums();
    }


    @PostMapping(value = "/addStadium", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StadiumDto> addStadium(
            @RequestPart("stadium") String stadiumJson,
            @RequestPart("file") MultipartFile file
    ) {
        System.out.println("Stadium JSON: " + stadiumJson);
        System.out.println("File: " + file.getOriginalFilename());

        ObjectMapper objectMapper = new ObjectMapper();
        StadiumDto stadiumDto;
        try {
            stadiumDto = objectMapper.readValue(stadiumJson, StadiumDto.class);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (file != null && !file.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            try {
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                String imageUrl = "/api/v1/uploads/images/" + fileName;
                stadiumDto.setImageUrl(imageUrl);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        Stadium saved = stadiumService.addStadium(stadiumDto);
        return ResponseEntity.ok(mapToDto(saved));
    }


    private StadiumDto mapToDto(Stadium stadium) {
        StadiumDto dto = new StadiumDto();
        dto.setName(stadium.getName());
        dto.setCapacity(stadium.getCapacity());
        dto.setDescription(stadium.getDescription());
        dto.setImageUrl(stadium.getImageUrl());
        dto.setCountry(stadium.getCountry());
        dto.setCity(stadium.getCity());
        return dto;
    }



    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public ResponseEntity<Void> deleteTeam(@PathVariable UUID id) {
        stadiumService.deleteStadium(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public ResponseEntity<StadiumDto> update(@PathVariable UUID id, @RequestBody StadiumDto dto) {
        return ResponseEntity.ok(stadiumService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StadiumDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(stadiumService.getById(id));
    }

    @GetMapping("/paged")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<StadiumDto>> GetPagedStadium(@RequestParam int page, @RequestParam int size, @RequestParam(required = false) String search) {
        Page<StadiumDto> stadiums = stadiumService.getStadiumsPaged(page, size, search);
        return ResponseEntity.ok(stadiums);
    }
}
