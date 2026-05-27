package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Role;
import com.mobylab.springbackend.exception.BadRequestException;
import com.mobylab.springbackend.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    void addRolesShouldThrowBadRequestWhenListIsEmpty() {
        assertThatThrownBy(() -> roleService.addRoles(List.of()))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Role list shouldn't be empty");

        verify(roleRepository, never()).save(org.mockito.Mockito.any());
    }

    @Test
    void addRolesShouldSaveOnlyRolesThatDoNotExist() {
        when(roleRepository.findRoleByName("USER")).thenReturn(Optional.of(new Role().setName("USER")));
        when(roleRepository.findRoleByName("MANAGER")).thenReturn(Optional.empty());

        List<String> result = roleService.addRoles(List.of("USER", "MANAGER"));

        assertThat(result).containsExactly("MANAGER");
        ArgumentCaptor<Role> roleCaptor = ArgumentCaptor.forClass(Role.class);
        verify(roleRepository).save(roleCaptor.capture());
        assertThat(roleCaptor.getValue().getName()).isEqualTo("MANAGER");
    }
}
