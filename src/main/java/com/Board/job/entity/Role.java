package com.Board.job.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    EMPLOYER, JOBSEEKER;

    @Override
    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
