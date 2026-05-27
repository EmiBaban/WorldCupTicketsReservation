package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.service.UserService;
import com.mobylab.springbackend.service.dto.RoleUpdateRequest;
import com.mobylab.springbackend.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @GetMapping("/paged")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<UserDto>> GetPagedUsers(@RequestParam int page, @RequestParam int size, @RequestParam(required = false) String search) {
        Page<UserDto> users = userService.getUsersPaged(page, size, search);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}/getById")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDto> GetUser(@PathVariable UUID userId) {
        UserDto user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/by-email")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email) {
        UserDto user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PutMapping("{userId}/toogleManagerRole")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDto> toggleManagerRole(@PathVariable UUID userId) {
        UserDto user =  userService.updateUserRoleManager(userId);
        return ResponseEntity.ok(user);
    }
}
