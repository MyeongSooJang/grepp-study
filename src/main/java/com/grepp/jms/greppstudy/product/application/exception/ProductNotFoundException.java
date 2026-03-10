package com.grepp.jms.greppstudy.product.application.exception;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(UUID productId) {
        super("Product not found. productId=" + productId);
    }
}
