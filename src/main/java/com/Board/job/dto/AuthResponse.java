package com.Board.job.dto;

import com.Board.job.entity.Role;

public record AuthResponse(String token , String email, Role role) {
}
