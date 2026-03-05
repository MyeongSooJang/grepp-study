package com.grepp.jms.greppstudy.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record ProductRequest(
        @Schema(description = "판매자 ID", example = "550e8400-e29b-41d4-a716-446655440000")
        String sellerId,

        @Schema(description = "상품명", example = "맥북 프로 16인치", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @Schema(description = "상품 설명", example = "2024년형 M3 Max 칩 탑재")
        String description,

        @Schema(description = "가격", example = "2500000", requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal price,

        @Schema(description = "재고 수량", example = "100")
        Long stock,

        @Schema(description = "상품 상태", example = "ACTIVE")
        String status)
{
}
