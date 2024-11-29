package com.ureca.picky_be.base.implementation.auth;

import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.jpa.user.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthManager {
    public Long getUserId(){
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
    }
    public Role getUserRole(){
        return Role.fromString(SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED)));

    }
}
