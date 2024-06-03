package com.khanh.antimessenger.service;

import com.khanh.antimessenger.model.Role;

import java.util.Collection;

public interface RoleService {
    Role getRoleByUserName(String username);

    Role getRoleByUserId(Long id);

    Role getRoleByRoleName(String roleName);

    Collection<Role> getAllRole();
}
