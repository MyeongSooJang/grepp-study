package com.grepp.jms.greppstudy.product.repo;

import com.grepp.jms.greppstudy.product.domain.Product;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, UUID> {
    List<Product> findAll(String name);
}
