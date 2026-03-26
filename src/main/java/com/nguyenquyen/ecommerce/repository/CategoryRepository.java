package com.nguyenquyen.ecommerce.repository;

import com.nguyenquyen.ecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
