package com.mobylab.springbackend.controller;


import com.mobylab.springbackend.entity.Stadium;
import com.mobylab.springbackend.entity.Team;
import com.mobylab.springbackend.service.StadiumService;
import com.mobylab.springbackend.service.TeamService;
import com.mobylab.springbackend.service.dto.StadiumDto;
import com.mobylab.springbackend.service.dto.TeamDto;
import com.mobylab.springbackend.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stadiums")
public class StadiumController implements SecuredRestController {

    @Autowired
    private StadiumService stadiumService;

    @GetMapping("/getAllStadiums")
    public List<StadiumDto> getAllStadiums() {
        return stadiumService.getAllStadiums();
    }

    @PostMapping("/addStadium")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public ResponseEntity<Stadium> addStadium(@RequestBody StadiumDto stadiumDto) {
        return ResponseEntity.ok(stadiumService.addStadium(stadiumDto));
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
}
