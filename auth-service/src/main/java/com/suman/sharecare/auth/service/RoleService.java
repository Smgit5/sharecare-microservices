package com.suman.sharecare.auth.service;

import com.suman.sharecare.auth.entity.Role;
import com.suman.sharecare.auth.exception.ResourceNotFoundException;
import com.suman.sharecare.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    public static final String CITIZEN = "CITIZEN";
    public static final String NGO_REP = "NGO_REP";
    public static final String ADMIN = "ADMIN";

    private final RoleRepository roleRepository;

    public Role getRoleByName(String role) {
        return roleRepository.findByName(role).orElseThrow(() -> new ResourceNotFoundException("Role not found!"));
    }

}
