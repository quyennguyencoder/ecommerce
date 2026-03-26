package com.nguyenquyen.ecommerce.repository;

import com.nguyenquyen.ecommerce.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    @Query("""
            SELECT u FROM User u LEFT JOIN u.role r
            WHERE (:keyword IS NULL 
                OR LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND (:role IS NULL OR LOWER(r.name) = LOWER(:role))
            """)
    Page<User> findAll(@Param("keyword") String keyword, @Param("role") String role, Pageable pageable);

    Optional<User> findByEmail(String subject);

    Optional<User> findByPhone(String subject);
}
