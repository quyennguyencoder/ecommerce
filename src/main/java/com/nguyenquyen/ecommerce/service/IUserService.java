package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.PaginationResponse;
import com.nguyenquyen.ecommerce.dto.request.UserCreateRequest;
import com.nguyenquyen.ecommerce.dto.request.UserRegisterByPhoneRequest;
import com.nguyenquyen.ecommerce.dto.request.UserChangePasswordRequest;
import com.nguyenquyen.ecommerce.dto.request.UserUpdateRequest;
import com.nguyenquyen.ecommerce.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService {
    UserResponse createUser(UserCreateRequest request);
    UserResponse getUserById(Long id);
    PaginationResponse<UserResponse> getAllUsers(String keyword, String roleName, int page, int size);
    UserResponse updateUser(Long id, UserUpdateRequest request);
    void deleteUser(Long id);
    void updateUserRole(Long userId, Long roleId);
    void deactivateUser(Long id);
    void activateUser(Long id);
    UserResponse uploadUserAvatar(Long id, MultipartFile avatar);
    void changePassword(Long userId, UserChangePasswordRequest request);
    UserResponse getMyProfile();
}
