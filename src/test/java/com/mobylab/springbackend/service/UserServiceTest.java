package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Role;
import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.exception.NotFoundException;
import com.mobylab.springbackend.repository.RoleRepository;
import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.service.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getAllUsersShouldMapUsersToDtosWithRoles() {
        User user = new User()
                .setId(UUID.randomUUID())
                .setUsername("tester")
                .setEmail("test@example.com")
                .setRoles(List.of(role("USER"), role("MANAGER")));

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDto> result = userService.getAllUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(user.getId());
        assertThat(result.get(0).getUsername()).isEqualTo("tester");
        assertThat(result.get(0).getEmail()).isEqualTo("test@example.com");
        assertThat(result.get(0).getRoles()).containsExactly("USER", "MANAGER");
    }

    @Test
    void getUserByIdShouldThrowNotFoundWhenUserMissing() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User not found!");
    }

    @Test
    void assignRoleToUserShouldAddRoleWhenUserDoesNotHaveIt() {
        UUID userId = UUID.randomUUID();
        User user = new User().setRoles(new ArrayList<>(List.of(role("USER"))));
        Role managerRole = role("MANAGER");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findRoleByName("MANAGER")).thenReturn(Optional.of(managerRole));

        userService.assignRoleToUser(userId, "MANAGER");

        assertThat(user.getRoles()).extracting(Role::getName).containsExactly("USER", "MANAGER");
        verify(userRepository).save(user);
    }

    @Test
    void assignRoleToUserShouldNotDuplicateRoleWhenAlreadyPresent() {
        UUID userId = UUID.randomUUID();
        Role managerRole = role("MANAGER");
        User user = new User().setRoles(new ArrayList<>(List.of(managerRole)));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findRoleByName("MANAGER")).thenReturn(Optional.of(managerRole));

        userService.assignRoleToUser(userId, "MANAGER");

        assertThat(user.getRoles()).containsExactly(managerRole);
        verify(userRepository, never()).save(user);
    }

    @Test
    void updateUserRoleManagerShouldAddManagerRoleWhenMissing() {
        UUID userId = UUID.randomUUID();
        User user = new User()
                .setId(userId)
                .setUsername("tester")
                .setEmail("test@example.com")
                .setRoles(new ArrayList<>(List.of(role("USER"))));
        Role managerRole = role("MANAGER");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findRoleByName("MANAGER")).thenReturn(Optional.of(managerRole));

        UserDto result = userService.updateUserRoleManager(userId);

        assertThat(result.getRoles()).containsExactly("USER", "MANAGER");
        verify(userRepository).save(user);
    }

    @Test
    void updateUserRoleManagerShouldRemoveManagerRoleWhenPresent() {
        UUID userId = UUID.randomUUID();
        Role managerRole = role("MANAGER");
        User user = new User()
                .setId(userId)
                .setUsername("tester")
                .setEmail("test@example.com")
                .setRoles(new ArrayList<>(List.of(role("USER"), managerRole)));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findRoleByName("MANAGER")).thenReturn(Optional.of(managerRole));

        UserDto result = userService.updateUserRoleManager(userId);

        assertThat(result.getRoles()).containsExactly("USER");
        verify(userRepository).save(user);
    }

    private static Role role(String name) {
        return new Role().setName(name);
    }
}
