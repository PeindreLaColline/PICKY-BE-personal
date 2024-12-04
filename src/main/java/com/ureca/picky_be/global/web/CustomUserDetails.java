package com.ureca.picky_be.global.web;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String username;
    private final String nickname;
    private final String role;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Long id, String username, String nickname, String role, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.role = role;
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return ""; // 비밀번호는 사용하지 않음
    }
}
