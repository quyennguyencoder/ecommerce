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
        return (subject.contains("@")
                ? userRepository.findByEmail(subject)
                : userRepository.findByPhone(subject))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email or phone: " + subject));
    }
}
