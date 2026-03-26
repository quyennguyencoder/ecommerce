package com.nguyenquyen.ecommerce.repository;

import com.nguyenquyen.ecommerce.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
