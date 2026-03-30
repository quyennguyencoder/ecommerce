package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.RoleCreateRequest;
import com.nguyenquyen.ecommerce.dto.response.RoleResponse;

import java.util.List;

public interface IRoleService {
    RoleResponse createRole(RoleCreateRequest request);
    List<RoleResponse> getAllRoles();
}
