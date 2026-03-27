package com.nguyenquyen.ecommerce.util;

import com.nguyenquyen.ecommerce.model.User;
import com.nguyenquyen.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            throw new RuntimeException("User chưa được xác thực");
//        }
//        Object principal = authentication.getPrincipal();
//        if (!(principal instanceof User)) {
//            throw new RuntimeException("User không hợp lệ");
//        }
//        return (User) principal;
        return userRepository.findById(1L).orElseThrow(() -> new RuntimeException("User không tồn tại"));

    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
