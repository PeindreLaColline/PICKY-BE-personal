package com.ureca.picky_be.base.business.auth;

import com.ureca.picky_be.base.implementation.user.UserManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtAuthService implements AuthUseCase{
    private final UserManager userManager;
}
