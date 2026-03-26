package com.nguyenquyen.ecommerce.service.impl;

import com.nguyenquyen.ecommerce.dto.request.auth.RegisterByEmailRequest;
import com.nguyenquyen.ecommerce.dto.request.auth.RegisterByPhoneRequest;
import com.nguyenquyen.ecommerce.dto.request.user.UserUpdateRequest;
import com.nguyenquyen.ecommerce.dto.response.UserResponse;
import com.nguyenquyen.ecommerce.mapper.RoleMapper;
import com.nguyenquyen.ecommerce.mapper.UserMapper;
import com.nguyenquyen.ecommerce.model.Role;
import com.nguyenquyen.ecommerce.model.User;
import com.nguyenquyen.ecommerce.repository.RoleRepository;
import com.nguyenquyen.ecommerce.repository.UserRepository;
import com.nguyenquyen.ecommerce.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;




    @Override
    public UserResponse registerByEmail(RegisterByEmailRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        User newUser = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .gender(request.getGender())
                .address(request.getAddress())
                .dob(request.getDob())
                .role(role)
                .isActive(true)
                .build();


        User savedUser = userRepository.save(newUser);
        return userMapper.userToUserResponse(savedUser);
    }

    @Override
    public UserResponse registerByPhone(RegisterByPhoneRequest request) {
        if(userRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Email already exists");
        }
        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        User newUser = User.builder()
                .phone(request.getPhone())
                .password(request.getPassword())
                .name(request.getName())
                .gender(request.getGender())
                .address(request.getAddress())
                .dob(request.getDob())
                .role(role)
                .isActive(true)
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
    public List<UserResponse> getAllUsers(String keyword, String role, Pageable pageable) {
        Page<User> userPage = userRepository.findAll(keyword, role, pageable);
        return userPage.map(user -> userMapper.userToUserResponse(user))
                        .getContent();
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

        if (request.getAvatar() != null) {
            existingUser.setAvatar(request.getAvatar());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
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

        existingUser.setIsActive(false);
        userRepository.save(existingUser);
    }

    @Override
    public void activateUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.setIsActive(true);
        userRepository.save(existingUser);
    }
}
