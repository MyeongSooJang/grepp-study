package com.grepp.jms.greppstudy.search.presentation.dto.response;

import java.util.List;

public record ProductSuggestResponse(
        List<ProductSuggestItemResponse> items
) {
}
