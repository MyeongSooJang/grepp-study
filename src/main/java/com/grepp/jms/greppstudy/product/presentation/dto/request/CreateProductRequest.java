package com.grepp.jms.greppstudy.product.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "상품 생성 요청")
public record CreateProductRequest(
        @NotNull
        @Schema(description = "판매자 ID", example = "11111111-1111-1111-1111-111111111111")
        UUID sellerId,

        @NotBlank
        @Size(max = 100)
        @Schema(description = "상품명", example = "맥북 프로 14")
        String name,

        @Schema(description = "상품 설명", example = "M3 칩셋, 16GB RAM")
        String description,

        @NotNull
        @DecimalMin(value = "0.0")
        @Schema(description = "가격", example = "2590000.00")
        BigDecimal price,

        @NotNull
        @Min(0)
        @Schema(description = "재고", example = "10")
        Integer stock,

        @NotBlank
        @Size(max = 20)
        @Schema(description = "상태", example = "ACTIVE")
        String status
) {
}
