package com.grepp.jms.greppstudy.product.domain.repository;

import com.grepp.jms.greppstudy.product.domain.model.Product;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(UUID productId);

    List<Product> findAll();

    void delete(Product product);
}
