package com.nguyenquyen.ecommerce.repository;

import com.nguyenquyen.ecommerce.model.SocialAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
}
