package com.grepp.jms.greppstudy.product.service;

import com.grepp.jms.greppstudy.product.domain.Product;
import com.grepp.jms.greppstudy.product.dto.ProductRequest;
import com.grepp.jms.greppstudy.product.dto.ProductResponse;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<Product> findAll();

    Product findById(UUID id);

    void deleteById(UUID id);

    ProductResponse create(ProductRequest request);

    Product update(UUID id, ProductRequest request);

}
