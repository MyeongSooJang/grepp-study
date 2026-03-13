package com.grepp.jms.greppstudy.search.infrastructure;

import com.grepp.jms.greppstudy.search.infrastructure.dto.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, String> {
}
