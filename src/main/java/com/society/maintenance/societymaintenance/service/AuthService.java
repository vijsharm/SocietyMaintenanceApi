package com.society.maintenance.societymaintenance.service;

import com.society.maintenance.societymaintenance.dto.LoginRequest;
import com.society.maintenance.societymaintenance.dto.LoginResponse;
import com.society.maintenance.societymaintenance.dto.Role;
import com.society.maintenance.societymaintenance.entity.User;
import com.society.maintenance.societymaintenance.repository.UserRepository;
import com.society.maintenance.societymaintenance.util.JwtUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final PasswordEncoder encoder;

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user);
        LoginResponse.UserInfo userInfo =
                new LoginResponse.UserInfo(
                        user.getId().toString(),
                        user.getUsername(),
                        mapRole(user.getRole()),
                        user.getUsername()
                );

        return new LoginResponse(token, true, userInfo);
    }

    private String mapRole(Role role) {
        return role == Role.ROLE_ADMIN ? "admin" : "readonly";
    }

    @PostConstruct
    void loadUsers() {
        if (userRepository.count() == 0) {

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("admin@123"));
            admin.setRole(Role.ROLE_ADMIN);

            User viewer = new User();
            viewer.setUsername("viewer");
            viewer.setPassword(encoder.encode("viewer@123"));
            viewer.setRole(Role.ROLE_VIEWER);

            userRepository.saveAll(List.of(admin, viewer));
        }
    }
}

