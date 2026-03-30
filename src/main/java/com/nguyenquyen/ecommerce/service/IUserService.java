package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.auth.RegisterByEmailRequest;
import com.nguyenquyen.ecommerce.dto.request.auth.RegisterByPhoneRequest;
import com.nguyenquyen.ecommerce.dto.request.ChangePasswordRequest;
import com.nguyenquyen.ecommerce.dto.request.UpdateUserRequest;
import com.nguyenquyen.ecommerce.dto.response.UserResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserService {
    UserResponse registerByEmail(RegisterByEmailRequest request);
    UserResponse registerByPhone(RegisterByPhoneRequest request);
    UserResponse getUserById(Long id);
    List<UserResponse> getAllUsers(String keyword, String roleName, int page, int size);
    UserResponse updateUser(Long id, UpdateUserRequest request);
    void deleteUser(Long id);
    void updateUserRole(Long userId, Long roleId);
    void deactivateUser(Long id);
    void activateUser(Long id);
    UserResponse uploadUserAvatar(Long id, MultipartFile avatar);
    Resource getAvatarFile(Long id);
    void changePassword(Long userId, ChangePasswordRequest request);
}
