package com.grepp.jms.greppstudy.product.application.exception;

import java.util.UUID;

public class SellerNotFoundException extends RuntimeException {

    public SellerNotFoundException(UUID sellerId) {
        super("Seller not found. sellerId=" + sellerId);
    }
}
