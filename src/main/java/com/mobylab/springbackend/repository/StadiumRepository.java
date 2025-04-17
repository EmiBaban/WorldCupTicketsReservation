package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StadiumRepository extends JpaRepository<Stadium, UUID> {
    Optional<Stadium> findStadiumByName(String name);
}
