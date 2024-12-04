package com.ureca.picky_be.global.web;

import com.ureca.picky_be.base.persistence.user.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Long userId = Long.parseLong(username);

        return userRepository.findById(userId)
                .map(user -> new CustomUserDetails(
                        user.getId(),
                        user.getName(),
                        user.getNickname(),
                        user.getRole().name(),
                        List.of(new SimpleGrantedAuthority(user.getRole().name()))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + username));
    }
}

