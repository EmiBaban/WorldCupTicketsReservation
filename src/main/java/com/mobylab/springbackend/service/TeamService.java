package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Team;
import com.mobylab.springbackend.exception.NotFoundException;
import com.mobylab.springbackend.repository.TeamRepository;
import com.mobylab.springbackend.service.dto.TeamDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<TeamDto> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(team -> {
                    TeamDto dto = new TeamDto();
                    dto.setName(team.getName());
                    dto.setFlagUrl(team.getFlagUrl());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Team addTeam(TeamDto teamDto) {
        Team team = new Team();
        team.setName(teamDto.getName());
        team.setFlagUrl(teamDto.getFlagUrl());
        return teamRepository.save(team);
    }

    public void deleteTeam(UUID id) {
        if (!teamRepository.existsById(id)) {
            throw new NotFoundException("Team not found");
        }

        teamRepository.deleteById(id);
    }

}
