package com.mobylab.springbackend.service;

import com.mobylab.springbackend.config.security.JwtGenerator;
import com.mobylab.springbackend.entity.Role;
import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.exception.BadRequestException;
import com.mobylab.springbackend.repository.RoleRepository;
import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.service.dto.LoginDto;
import com.mobylab.springbackend.service.dto.RegisterDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtGenerator jwtGenerator;

    @InjectMocks
    private AuthService authService;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void registerShouldThrowBadRequestWhenEmailAlreadyExists() {
        RegisterDto dto = new RegisterDto()
                .setEmail("test@example.com")
                .setUsername("tester")
                .setPassword("password");

        when(userRepository.existsUserByEmail("test@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(dto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Email is already used");

        verify(userRepository, never()).save(any());
    }

    @Test
    void registerShouldSaveUserWithEncodedPasswordAndUserRole() {
        RegisterDto dto = new RegisterDto()
                .setEmail("test@example.com")
                .setUsername("tester")
                .setPassword("plain-password");
        Role userRole = new Role().setName("USER");

        when(userRepository.existsUserByEmail("test@example.com")).thenReturn(false);
        when(roleRepository.findRoleByName("USER")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode("plain-password")).thenReturn("encoded-password");

        authService.register(dto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.getUsername()).isEqualTo("tester");
        assertThat(savedUser.getPassword()).isEqualTo("encoded-password");
        assertThat(savedUser.getRoles()).containsExactly(userRole);
    }

    @Test
    void loginShouldThrowBadRequestWhenUserDoesNotExist() {
        LoginDto dto = new LoginDto()
                .setEmail("missing@example.com")
                .setPassword("password");

        when(userRepository.findUserByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(dto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Wrong credentials");

        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    void loginShouldAuthenticateAndReturnJwtWhenCredentialsAreValid() {
        LoginDto dto = new LoginDto()
                .setEmail("test@example.com")
                .setPassword("password");
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);

        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(new User()));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtGenerator.generateToken(authentication)).thenReturn("jwt-token");

        String token = authService.login(dto);

        assertThat(token).isEqualTo("jwt-token");
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isSameAs(authentication);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
