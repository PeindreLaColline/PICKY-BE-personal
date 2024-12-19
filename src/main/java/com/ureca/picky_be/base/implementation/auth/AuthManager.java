package com.ureca.picky_be.base.implementation.auth;

import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.global.web.CustomUserDetails;
import com.ureca.picky_be.jpa.entity.user.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthManager {

    private CustomUserDetails getCurrentUserDetails() {
        return (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
    public Long getUserId() {
        return getCurrentUserDetails().getId();
    }

    public Role getUserRole() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(Role::fromString)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));
    }

    public String getUserNickname() {
        return getCurrentUserDetails().getNickname();
    }

    public String getUserName() {
        return getCurrentUserDetails().getUsername(); // name == username
    }
}
