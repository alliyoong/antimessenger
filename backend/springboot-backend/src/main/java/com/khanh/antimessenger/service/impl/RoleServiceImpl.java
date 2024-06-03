package com.khanh.antimessenger.service.impl;

import com.khanh.antimessenger.exception.ResourceNotFoundException;
import com.khanh.antimessenger.model.Role;
import com.khanh.antimessenger.repository.RoleRepository;
import com.khanh.antimessenger.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByUserName(String username) {
        return null;
    }

    @Override
    public Role getRoleByUserId(Long id) {
        return null;
    }

    @Override
    public Role getRoleByRoleName(String roleName) {
        return roleRepository.findRoleByRoleName(roleName).orElseThrow(
                () -> new ResourceNotFoundException("Role", "roleName", roleName)
        );
    }

    @Override
    public Collection<Role> getAllRole() {
        return roleRepository.findAll();
    }
}
