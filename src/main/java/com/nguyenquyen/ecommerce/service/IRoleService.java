package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.role.RoleCreateRequest;
import com.nguyenquyen.ecommerce.dto.request.role.RoleUpdateRequest;
import com.nguyenquyen.ecommerce.dto.response.RoleResponse;

import java.util.List;

public interface IRoleService {
    RoleResponse createRole(RoleCreateRequest request);
    List<RoleResponse> getAllRoles();
}
