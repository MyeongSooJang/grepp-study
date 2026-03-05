package com.grepp.jms.greppstudy.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponse(UUID id,
                              BigDecimal price,
                              Long stock,
                              String status,
                              LocalDateTime modifyDate) {

}