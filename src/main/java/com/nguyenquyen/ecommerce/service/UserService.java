package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dtos.UserDTO;
import com.nguyenquyen.ecommerce.exception.DataNotFoundException;
import com.nguyenquyen.ecommerce.models.Role;
import com.nguyenquyen.ecommerce.models.User;
import com.nguyenquyen.ecommerce.repository.RoleRepository;
import com.nguyenquyen.ecommerce.repository.UserRepository;
import com.nguyenquyen.ecommerce.service.intf.IUserService;
import com.nguyenquyen.ecommerce.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException {
        String phoneNumber = userDTO.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new RuntimeException("Phone number already exists");
        }
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();

        newUser.setRole(role);
        if(userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
            String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
            newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) {
        User existingUser = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));
//        if(existingUser.getFacebookAccountId() == 0 && existingUser.getGoogleAccountId() == 0) {
//            if(!passwordEncoder.matches(password, existingUser.getPassword())) {
//                throw new RuntimeException("Invalid password");
//            }
//        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(phoneNumber, password)
        );
        return jwtUtil.generateToken(existingUser);
    }
}
