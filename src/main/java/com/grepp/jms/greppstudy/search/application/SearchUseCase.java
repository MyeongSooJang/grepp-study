package com.grepp.jms.greppstudy.search.application;

import com.grepp.jms.greppstudy.search.infrastructure.dto.ProductDocument;
import com.grepp.jms.greppstudy.search.presentation.dto.request.IndexConfigRequest;
import com.grepp.jms.greppstudy.search.presentation.dto.request.ProductIndexRequest;
import com.grepp.jms.greppstudy.search.presentation.dto.response.IndexStatusResponse;
import com.grepp.jms.greppstudy.search.presentation.dto.response.IndexUpdateResponse;
import com.grepp.jms.greppstudy.search.presentation.dto.response.ProductFilterAggregationResponse;
import com.grepp.jms.greppstudy.search.presentation.dto.response.ProductSearchResponse;
import com.grepp.jms.greppstudy.search.presentation.dto.response.ProductSuggestResponse;
import org.springframework.data.domain.Pageable;

public interface SearchUseCase {
    ProductDocument indexProduct(ProductIndexRequest request);
    IndexUpdateResponse applyProductIndexConfig(IndexConfigRequest request);
    IndexStatusResponse getProductIndexStatus();
    ProductSearchResponse searchProducts(String keyword, String category, Pageable pageable);
    ProductSuggestResponse suggestProducts(String keyword, int size);
    ProductFilterAggregationResponse aggregateProductFilters(String keyword);


}
