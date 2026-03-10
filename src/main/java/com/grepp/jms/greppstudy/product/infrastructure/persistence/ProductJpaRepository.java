package com.grepp.jms.greppstudy.product.infrastructure.persistence;

import com.grepp.jms.greppstudy.product.domain.model.Product;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, UUID> {
}
