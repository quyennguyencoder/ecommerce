package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.UserRegisterByEmailRequest;
import com.nguyenquyen.ecommerce.dto.request.UserRegisterByPhoneRequest;
import com.nguyenquyen.ecommerce.dto.request.UserChangePasswordRequest;
import com.nguyenquyen.ecommerce.dto.request.UserUpdateRequest;
import com.nguyenquyen.ecommerce.dto.response.UserResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserService {
    UserResponse registerByEmail(UserRegisterByEmailRequest request);
    UserResponse registerByPhone(UserRegisterByPhoneRequest request);
    UserResponse getUserById(Long id);
    List<UserResponse> getAllUsers(String keyword, String roleName, int page, int size);
    UserResponse updateUser(Long id, UserUpdateRequest request);
    void deleteUser(Long id);
    void updateUserRole(Long userId, Long roleId);
    void deactivateUser(Long id);
    void activateUser(Long id);
    UserResponse uploadUserAvatar(Long id, MultipartFile avatar);
    Resource getAvatarFile(Long id);
    void changePassword(Long userId, UserChangePasswordRequest request);
}
