package com.Board.job.service;

import com.Board.job.Repository.UserRepository;
import com.Board.job.dto.AuthRequest;
import com.Board.job.dto.AuthResponse;
import com.Board.job.dto.RegisterRequest;
import com.Board.job.entity.Role;
import com.Board.job.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

       User user = new User();
                user.setEmail(request.email());
                user.setPassword(passwordEncoder.encode(request.password()));
                user.setRole(Role.valueOf(request.role()));

        userRepository.save(user);

        String jwt = jwtService.generateToken(user);
        return new AuthResponse(jwt, user.getEmail(), user.getRole());
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        String jwt = jwtService.generateToken(user);
        return new AuthResponse(jwt, user.getEmail(), user.getRole());
    }


}
