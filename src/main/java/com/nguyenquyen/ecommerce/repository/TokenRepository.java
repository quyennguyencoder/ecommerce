package com.nguyenquyen.ecommerce.repository;

import com.nguyenquyen.ecommerce.model.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<Token, String> {
}