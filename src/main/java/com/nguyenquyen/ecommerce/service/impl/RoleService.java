package com.nguyenquyen.ecommerce.service.impl;

import com.nguyenquyen.ecommerce.dto.request.RoleCreateRequest;
import com.nguyenquyen.ecommerce.dto.response.RoleResponse;
import com.nguyenquyen.ecommerce.mapper.RoleMapper;
import com.nguyenquyen.ecommerce.model.Role;
import com.nguyenquyen.ecommerce.repository.RoleRepository;
import com.nguyenquyen.ecommerce.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


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
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::roleToRoleResponse)
                .collect(Collectors.toList());
    }
}
