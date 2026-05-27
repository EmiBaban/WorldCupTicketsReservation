package com.mobylab.springbackend.repository;

import com.mobylab.springbackend.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

public interface UserRepository extends JpaRepository<User, UUID> {
    Boolean existsUserByEmail(String email);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUsersByUsername(String username);
    Optional<User> findUsersById(UUID id);
    Page<User> findAll(Specification<User> specification, Pageable pageable);
}
