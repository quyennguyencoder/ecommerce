package com.nguyenquyen.ecommerce.service.impl;

import com.nguyenquyen.ecommerce.dto.request.auth.RegisterByEmailRequest;
import com.nguyenquyen.ecommerce.dto.request.auth.RegisterByPhoneRequest;
import com.nguyenquyen.ecommerce.dto.response.UserResponse;
import com.nguyenquyen.ecommerce.mapper.RoleMapper;
import com.nguyenquyen.ecommerce.mapper.UserMapper;
import com.nguyenquyen.ecommerce.model.Role;
import com.nguyenquyen.ecommerce.model.User;
import com.nguyenquyen.ecommerce.repository.RoleRepository;
import com.nguyenquyen.ecommerce.repository.UserRepository;
import com.nguyenquyen.ecommerce.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

}
