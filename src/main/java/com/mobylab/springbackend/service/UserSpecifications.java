package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class UserSpecifications {

    public static Specification<User> hasSearchTerm(String search) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(search)) {
                return criteriaBuilder.or(
                        criteriaBuilder.like(root.get("username"), "%" + search + "%"),
                        criteriaBuilder.like(root.get("email"), "%" + search + "%")
                );
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }
}
