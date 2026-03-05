package com.grepp.jms.greppstudy.product.controller;

import com.grepp.jms.greppstudy.product.domain.Product;
import com.grepp.jms.greppstudy.product.dto.ProductRequest;
import com.grepp.jms.greppstudy.product.dto.ProductResponse;
import com.grepp.jms.greppstudy.product.service.ProductService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest request) {
        ProductResponse product = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }


    @PutMapping("/{productId}")
    public ResponseEntity<Product> update(@PathVariable String productId, @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.update(UUID.fromString(productId), request));
    }
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@PathVariable String productId) {
        productService.deleteById(UUID.fromString(productId));
        return ResponseEntity.noContent().build();
    }
}
