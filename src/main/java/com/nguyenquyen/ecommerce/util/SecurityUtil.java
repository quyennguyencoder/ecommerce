package com.nguyenquyen.ecommerce.util;

import com.nguyenquyen.ecommerce.model.User;
import com.nguyenquyen.ecommerce.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SecurityUtil {

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Jwt jwt = (Jwt) authentication.getPrincipal();
        String subject = jwt.getSubject(); // Tương đương jwt.getClaimAsString("sub")



        // 3. Logic rẽ nhánh phân loại cực chuẩn của bạn
        if (subject.contains("@")) {
            // Nếu chứa '@' thì chắc chắn là Email
            return userRepository.findByEmail(subject)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy User với email: " + subject));
        } else {
            // Ngược lại, xử lý như Số điện thoại
            return userRepository.findByPhone(subject)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy User với SĐT: " + subject));
        }
    }

    public HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new RuntimeException("Không có HttpServletRequest trong context hiện tại");
        }
        return requestAttributes.getRequest();
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
