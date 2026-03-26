package com.nguyenquyen.ecommerce.service.impl;

import com.nguyenquyen.ecommerce.dto.request.role.RoleCreateRequest;
import com.nguyenquyen.ecommerce.dto.request.role.RoleUpdateRequest;
import com.nguyenquyen.ecommerce.dto.response.RoleResponse;
import com.nguyenquyen.ecommerce.mapper.RoleMapper;
import com.nguyenquyen.ecommerce.model.Role;
import com.nguyenquyen.ecommerce.repository.RoleRepository;
import com.nguyenquyen.ecommerce.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleResponse createRole(RoleCreateRequest request) {
        Role newRole = Role.builder()
                .name(request.getName())
                .build();
        Role savedRole = roleRepository.save(newRole);
        return roleMapper.roleToRoleResponse(savedRole);
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        List<Role> allRoles = roleRepository.findAll();
        return allRoles.stream()
                .map(role -> roleMapper.roleToRoleResponse(role))
                .toList();
    }
}
