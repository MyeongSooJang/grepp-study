package com.grepp.jms.greppstudy.product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Product {
    @Id
    private UUID id;

    private UUID sellerId;

    private String name;

    private String description;

    private BigDecimal price;

    private Long stocks;

    private String status;

    private LocalDateTime modifyDate;

    public static Product create(UUID sellerId, String name, String description, BigDecimal price, Long stocks, String status) {
        return new Product(UUID.randomUUID(), sellerId, name, description, price, stocks, status, LocalDateTime.now());
    }

    public Product update(String name, BigDecimal price, Long stocks, String status, LocalDateTime modifyDate) {
        this.name = name;
        this.price = price;
        this.stocks = stocks;
        this.status = status;
        this.modifyDate = LocalDateTime.now();
        return this;
    }
}
