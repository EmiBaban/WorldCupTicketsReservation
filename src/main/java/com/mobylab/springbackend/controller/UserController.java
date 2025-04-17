package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.service.UserService;
import com.mobylab.springbackend.service.dto.RoleUpdateRequest;
import com.mobylab.springbackend.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController implements SecuredRestController {

    @Autowired
    private UserService userService;

    @GetMapping("/getAllUsers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> assignRole(@RequestBody RoleUpdateRequest request) {
        userService.assignRoleToUser(request.getUserId(), request.getRoleName());
        return ResponseEntity.ok("Role assigned");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        userService.removeUser(id);
        return ResponseEntity.ok("User deleted");
    }
}
