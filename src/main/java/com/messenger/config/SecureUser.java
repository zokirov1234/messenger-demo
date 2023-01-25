package com.messenger.config;


import com.messenger.model.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SecureUser implements UserDetails {

    private List<SimpleGrantedAuthority> userRoles;

    private String password;
    private String username;

    public SecureUser(UserEntity user) {
        this.userRoles = new ArrayList<>();
        user.getUserRoles().forEach(userRoleDTO -> this.userRoles.add(new SimpleGrantedAuthority(userRoleDTO.getRole())));

        this.password = user.getPassword();
        this.username = user.getUsername();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.userRoles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
}
