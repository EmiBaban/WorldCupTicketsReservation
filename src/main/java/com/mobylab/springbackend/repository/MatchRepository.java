package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.Match;
import com.mobylab.springbackend.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MatchRepository  extends JpaRepository<Match, UUID> {
}
