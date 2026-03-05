package com.grepp.jms.greppstudy.product.service;

import com.grepp.jms.greppstudy.product.domain.Product;
import com.grepp.jms.greppstudy.product.dto.ProductRequest;
import com.grepp.jms.greppstudy.product.dto.ProductResponse;
import com.grepp.jms.greppstudy.product.repo.ProductRepo;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;

    @Override
    public List<Product> findAll() {
        return productRepo.findAll();
    }

    @Override
    public Product findById(UUID id) {
        return null;
    }

    @Override
    public void deleteById(UUID id) {

    }

    @Override
    public ProductResponse create(ProductRequest request) {
        Product product = Product.create(toUuid(request.sellerId(), "sellerId"),
                request.name(),
                request.description(),
                request.price(),
                request.stock(),
                request.status());
        return ofProduct(productRepo.save(product));
    }

    private UUID toUuid(String value, String fieldName) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName + " must be valid UUID");
        }
    }

    private ProductResponse ofProduct(Product product) {
        return new ProductResponse(product.getId(), product.getPrice(), product.getStocks(), product.getStatus(),
                product.getModifyDate());
    }
}
