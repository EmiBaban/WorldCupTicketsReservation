package com.mobylab.springbackend.service.dto;

import com.mobylab.springbackend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Data
@NoArgsConstructor @AllArgsConstructor
public class UserDto {
    private UUID id;
    private String username;
    private String email;
    private List<String> roles;
}
