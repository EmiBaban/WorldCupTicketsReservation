package com.mobylab.springbackend.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddStadiumDto {
    private String name;
    private int capacity;
    private String description;
    private String country;
    private String city;
}
