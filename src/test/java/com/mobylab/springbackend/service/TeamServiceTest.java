package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Team;
import com.mobylab.springbackend.exception.NotFoundException;
import com.mobylab.springbackend.repository.TeamRepository;
import com.mobylab.springbackend.service.dto.TeamDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;

    @Test
    void getAllTeamsShouldMapTeamsToDtos() {
        Team team = new Team();
        team.setName("Romania");
        team.setFlagUrl("romania.png");

        when(teamRepository.findAll()).thenReturn(List.of(team));

        List<TeamDto> result = teamService.getAllTeams();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Romania");
        assertThat(result.get(0).getFlagUrl()).isEqualTo("romania.png");
    }

    @Test
    void addTeamShouldSaveTeam() {
        TeamDto dto = new TeamDto("Romania", "romania.png");
        when(teamRepository.save(org.mockito.Mockito.any(Team.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Team result = teamService.addTeam(dto);

        assertThat(result.getName()).isEqualTo("Romania");
        assertThat(result.getFlagUrl()).isEqualTo("romania.png");
    }

    @Test
    void deleteTeamShouldThrowNotFoundWhenTeamMissing() {
        UUID id = UUID.randomUUID();
        when(teamRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> teamService.deleteTeam(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Team not found");
    }

    @Test
    void deleteTeamShouldDeleteWhenTeamExists() {
        UUID id = UUID.randomUUID();
        when(teamRepository.existsById(id)).thenReturn(true);

        teamService.deleteTeam(id);

        verify(teamRepository).deleteById(id);
    }
}
