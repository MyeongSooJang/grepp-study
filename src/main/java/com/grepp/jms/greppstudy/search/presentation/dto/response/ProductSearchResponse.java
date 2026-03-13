package com.grepp.jms.greppstudy.search.presentation.dto.response;

import com.grepp.jms.greppstudy.search.infrastructure.dto.ProductDocument;
import java.util.List;

public record ProductSearchResponse(long total, List<ProductDocument> items) {
}
