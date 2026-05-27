package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.Match;
import com.mobylab.springbackend.entity.Team;
import com.mobylab.springbackend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MatchRepository  extends JpaRepository<Match, UUID> {
    Page<Match> findAll(Specification<Match> specification, Pageable pageable);
}
