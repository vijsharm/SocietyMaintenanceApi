package com.society.maintenance.societymaintenance.repository;

import com.society.maintenance.societymaintenance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

}

