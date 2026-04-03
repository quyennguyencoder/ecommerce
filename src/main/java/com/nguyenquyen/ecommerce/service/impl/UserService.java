package com.nguyenquyen.ecommerce.service.impl;

import com.nguyenquyen.ecommerce.dto.PaginationResponse;
import com.nguyenquyen.ecommerce.dto.request.UserCreateRequest;
import com.nguyenquyen.ecommerce.dto.request.UserRegisterByPhoneRequest;
import com.nguyenquyen.ecommerce.dto.request.UserChangePasswordRequest;
import com.nguyenquyen.ecommerce.dto.request.UserUpdateRequest;
import com.nguyenquyen.ecommerce.dto.response.UserResponse;
import com.nguyenquyen.ecommerce.mapper.RoleMapper;
import com.nguyenquyen.ecommerce.mapper.UserMapper;
import com.nguyenquyen.ecommerce.model.Role;
import com.nguyenquyen.ecommerce.model.User;
import com.nguyenquyen.ecommerce.repository.RoleRepository;
import com.nguyenquyen.ecommerce.repository.UserRepository;
import com.nguyenquyen.ecommerce.service.IFileService;
import com.nguyenquyen.ecommerce.service.IUserService;
import com.nguyenquyen.ecommerce.util.SecurityUtil;
import com.nguyenquyen.ecommerce.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;
    private final IFileService fileService;
    private final SecurityUtil securityUtil;




    @Override
    public UserResponse createUser(UserCreateRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        User newUser = User.builder()
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .gender(request.getGender())
                .address(request.getAddress())
                .dob(request.getDob())
                .role(role)
                .active(true)
                .build();


        User savedUser = userRepository.save(newUser);
        return userMapper.userToUserResponse(savedUser);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return userMapper.userToUserResponse(existingUser);
    }

    @Override
    public PaginationResponse<UserResponse> getAllUsers(String keyword, String role, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponse> userPage = userRepository.findAll(keyword, role, pageable)
                .map(user -> userMapper.userToUserResponse(user));
        return PaginationUtil.toPaginationResponse(userPage);
    }

    @Override
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (request.getEmail() != null && !request.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            existingUser.setEmail(request.getEmail());
        }

        if (request.getPhone() != null && !request.getPhone().equals(existingUser.getPhone())) {
            if (userRepository.existsByPhone(request.getPhone())) {
                throw new RuntimeException("Phone already exists");
            }
            existingUser.setPhone(request.getPhone());
        }

        if (request.getName() != null) {
            existingUser.setName(request.getName());
        }

        if (request.getAddress() != null) {
            existingUser.setAddress(request.getAddress());
        }

        if (request.getGender() != null) {
            existingUser.setGender(request.getGender());
        }

        if (request.getDob() != null) {
            existingUser.setDob(request.getDob());
        }

        User updatedUser = userRepository.save(existingUser);
        return userMapper.userToUserResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public void updateUserRole(Long userId, Long roleId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        existingUser.setRole(role);
        userRepository.save(existingUser);
    }

    @Override
    public void deactivateUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.setActive(false);
        userRepository.save(existingUser);
    }

    @Override
    public void activateUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.setActive(true);
        userRepository.save(existingUser);
    }


    @Override
    public UserResponse uploadUserAvatar(Long id, MultipartFile avatar) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (avatar != null && !avatar.isEmpty()) {
            String avatarFileName = fileService.uploadFile(avatar);
            existingUser.setAvatar(avatarFileName);
            userRepository.save(existingUser);
        }

        return userMapper.userToUserResponse(existingUser);
    }

    @Override
    public void changePassword(Long userId, UserChangePasswordRequest request) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Verify old password
        if (!passwordEncoder.matches(request.getOldPassword(), existingUser.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không chính xác");
        }

        // Verify new password and confirm password match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu mới và xác nhận mật khẩu không khớp");
        }

        // Update password
        existingUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(existingUser);
    }

    @Override
    public UserResponse getMyProfile() {
        User currentUser = securityUtil.getCurrentUser();
        return userMapper.userToUserResponse(currentUser);
    }
}
