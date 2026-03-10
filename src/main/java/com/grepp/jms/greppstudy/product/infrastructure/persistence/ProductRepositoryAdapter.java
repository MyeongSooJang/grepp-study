package com.grepp.jms.greppstudy.product.infrastructure.persistence;

import com.grepp.jms.greppstudy.product.domain.model.Product;
import com.grepp.jms.greppstudy.product.domain.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    public ProductRepositoryAdapter(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public Optional<Product> findById(UUID productId) {
        return productJpaRepository.findById(productId);
    }

    @Override
    public List<Product> findAll() {
        return productJpaRepository.findAll();
    }

    @Override
    public void delete(Product product) {
        productJpaRepository.delete(product);
    }
}
