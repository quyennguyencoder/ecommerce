package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.auth.RegisterByEmailRequest;
import com.nguyenquyen.ecommerce.dto.request.auth.RegisterByPhoneRequest;
import com.nguyenquyen.ecommerce.dto.request.user.ChangePasswordRequest;
import com.nguyenquyen.ecommerce.dto.request.user.UserUpdateRequest;
import com.nguyenquyen.ecommerce.dto.response.UserResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {
    UserResponse registerByEmail(RegisterByEmailRequest request);
    UserResponse registerByPhone(RegisterByPhoneRequest request);
    UserResponse getUserById(Long id);
    List<UserResponse> getAllUsers(String keyword, String roleName, Pageable pageable);
    UserResponse updateUser(Long id, UserUpdateRequest request);
    void deleteUser(Long id);
    void updateUserRole(Long userId, Long roleId);
    void deactivateUser(Long id);
    void activateUser(Long id);
    void updateUserAvatar(Long id, String avatarFileName);
    void changePassword(Long userId, ChangePasswordRequest request);
}
