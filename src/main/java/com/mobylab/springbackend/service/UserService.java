package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.Role;
import com.mobylab.springbackend.entity.Stadium;
import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.exception.BadRequestException;
import com.mobylab.springbackend.exception.NotFoundException;
import com.mobylab.springbackend.repository.RoleRepository;
import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.service.dto.StadiumDto;
import com.mobylab.springbackend.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;


    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(user -> {
            UserDto dto = new UserDto();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setRoles(user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }

    public Page<UserDto> getUsersPaged(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<User> spec = UserSpecifications.hasSearchTerm(search);

        return userRepository.findAll(spec, pageable).map(user -> {
            UserDto dto = new UserDto();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setRoles(user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toList()));
            return dto;
        });
    }

    public UserDto getUserById(UUID id) {
        return userRepository.findById(id)
                .map(user -> {
                    UserDto dto = new UserDto();
                    dto.setId(user.getId());
                    dto.setUsername(user.getUsername());
                    dto.setEmail(user.getEmail());
                    dto.setRoles(user.getRoles().stream()
                            .map(Role::getName)
                            .collect(Collectors.toList()));
                    return dto;
                })
                .orElseThrow(() -> new NotFoundException("User not found!"));
    }

    public UserDto getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .map(user -> {
                    UserDto dto = new UserDto();
                    dto.setId(user.getId());
                    dto.setUsername(user.getUsername());
                    dto.setEmail(user.getEmail());
                    dto.setRoles(user.getRoles().stream()
                            .map(Role::getName)
                            .collect(Collectors.toList()));
                    return dto;
                })
                .orElseThrow(() -> new NotFoundException("User not found!"));
    }

    private StadiumDto mapToDto(Stadium stadium) {
        StadiumDto dto = new StadiumDto();
        dto.setName(stadium.getName());
        dto.setCapacity(stadium.getCapacity());
        dto.setDescription(stadium.getDescription());
        return dto;
    }

    public void assignRoleToUser(UUID userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Role role = roleRepository.findRoleByName(roleName)
                .orElseThrow(() -> new NotFoundException("Role not found"));

        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepository.save(user);
        }
    }

    public UserDto updateUserRoleManager(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Role managerRole = roleRepository.findRoleByName("MANAGER")
                .orElseThrow(() -> new NotFoundException("Role 'MANAGER' not found"));

        if (user.getRoles().contains(managerRole)) {
            user.getRoles().remove(managerRole);
        } else {
            user.getRoles().add(managerRole);
        }

        userRepository.save(user);

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList()));

        return dto;
    }



    public void removeUser(UUID id) {
        userRepository.deleteById(id);
    }
}
