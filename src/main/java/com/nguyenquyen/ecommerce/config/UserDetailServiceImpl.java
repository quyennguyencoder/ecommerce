package com.nguyenquyen.ecommerce.config;

import com.nguyenquyen.ecommerce.model.User;
import com.nguyenquyen.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String subject) throws UsernameNotFoundException {
        Optional<User> userOptional;
        if (subject.contains("@")) {
            userOptional = userRepository.findByEmail(subject);
        } else {
            userOptional = userRepository.findByPhone(subject);
        }
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email or phone: " + subject);
        }
        return userOptional.get();
    }
}
