package com.example.bachelorarbeit.repository.user_management;

import com.example.bachelorarbeit.models.user_management.ERole;
import com.example.bachelorarbeit.models.user_management.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
