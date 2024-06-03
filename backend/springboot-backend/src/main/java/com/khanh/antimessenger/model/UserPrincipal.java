package com.khanh.antimessenger.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {
    private MessAccount messAccount;

    public UserPrincipal(MessAccount messAccount) {
        this.messAccount = messAccount;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.messAccount.getRole().getRoleName()));
    }

    @Override
    public String getPassword() {
        return this.messAccount.getPassword();
    }

    @Override
    public String getUsername() {
        return this.messAccount.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.messAccount.getIsNonLocked() == 1 ? true : false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.messAccount.getIsEnabled() == 1 ? true : false;
    }

}
