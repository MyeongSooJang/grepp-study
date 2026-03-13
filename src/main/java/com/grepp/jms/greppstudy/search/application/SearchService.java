package com.grepp.jms.greppstudy.search.application;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.RangeBucket;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import com.grepp.jms.greppstudy.search.infrastructure.ProductSearchRepository;
import com.grepp.jms.greppstudy.search.infrastructure.dto.ProductDocument;
import com.grepp.jms.greppstudy.search.presentation.dto.request.IndexConfigRequest;
import com.grepp.jms.greppstudy.search.presentation.dto.request.ProductIndexRequest;
import com.grepp.jms.greppstudy.search.presentation.dto.response.IndexStatusResponse;
import com.grepp.jms.greppstudy.search.presentation.dto.response.IndexUpdateResponse;
import com.grepp.jms.greppstudy.search.presentation.dto.response.ProductFilterAggregationResponse;
import com.grepp.jms.greppstudy.search.presentation.dto.response.ProductFilterBucketResponse;
import com.grepp.jms.greppstudy.search.presentation.dto.response.ProductSearchResponse;
import com.grepp.jms.greppstudy.search.presentation.dto.response.ProductSuggestItemResponse;
import com.grepp.jms.greppstudy.search.presentation.dto.response.ProductSuggestResponse;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.AggregationsContainer;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService implements SearchUseCase{

    private final ElasticsearchOperations operations;
    private final ProductSearchRepository repository;

    @Override
    public ProductDocument indexProduct(ProductIndexRequest request) {
        Instant updatedAt = Instant.now();
        ProductDocument doc = new ProductDocument(
                null, // id를 비우면 ES가 자동 생성
                request.name(),
                request.brand(),
                request.category(),
                request.price(),
                updatedAt
        );
        return repository.save(doc);
    }

    @Override
    public IndexUpdateResponse applyProductIndexConfig(IndexConfigRequest request) {
        IndexOperations ops = operations.indexOps(ProductDocument.class);
        boolean created = false;
        boolean settingsUpdated = false;
        boolean mappingUpdated = false;

        if (!ops.exists()) {
            Document settings = Document.create();
            if (request.numberOfShards() != null) {
                settings.put("index.number_of_shards", request.numberOfShards());
            }
            if (request.numberOfReplicas() != null) {
                settings.put("index.number_of_replicas", request.numberOfReplicas());
            }
            created = ops.create(settings);
            mappingUpdated = ops.putMapping(ops.createMapping(ProductDocument.class));
        } else {
            // 기존 인덱스는 샤드 수 변경이 불가. 레플리카/매핑 변경은 별도 관리 API에서 수행하거나 추후 확장.
            mappingUpdated = ops.putMapping(ops.createMapping(ProductDocument.class));
        }

        return new IndexUpdateResponse(created, settingsUpdated, mappingUpdated);
    }

    @Override
    public IndexStatusResponse getProductIndexStatus() {
        IndexOperations ops = operations.indexOps(ProductDocument.class);
        boolean exists = ops.exists();
        Map<String, Object> settings = exists ? new HashMap<>(ops.getSettings()) : Map.of();
        Map<String, Object> mapping = exists ? new HashMap<>(ops.getMapping()) : Map.of();
        return new IndexStatusResponse(exists, settings, mapping);
    }

    @Override
    public ProductSearchResponse searchProducts(String keyword, String category, Pageable pageable) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.bool(b -> {
                    if (keyword != null && !keyword.isBlank()) {
                        b.must(//조건이 유추가 아닌 정확하게 맞아야 함을 의미
                                m -> m.match(
                                        mm -> mm
                                                .field("name")
                                                .query(keyword)
                                                .operator(Operator.And) // "남자" AND "신발" 식으로 토큰 모두 매칭
                                ));
                    }
                    if (category != null && !category.isBlank()) {
                        b.filter(f -> f.term(// term으로 정확히 같은 카테고리 값만 매칭
                                t -> t.field("category").value(category)));
                    }
                    return b;
                }))
                .withPageable(pageable)
                .build();

        SearchHits<ProductDocument> hits = operations.search(query, ProductDocument.class);
        List<ProductDocument> items = hits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .toList();

        return new ProductSearchResponse(hits.getTotalHits(), items);
    }

    @Override
    public ProductFilterAggregationResponse aggregateProductFilters(String keyword) {
        // TODO 1단계: NativeQuery.builder()로 쿼리 생성
        // - .withQuery()로 검색 조건 추가 (keyword가 있으면)
        // - .withMaxResults(0) 추가 (문서는 필요없고 집계만)
        // - .withAggregation("brands", ...) 추가
        // - .withAggregation("categories", ...) 추가
        // - .withAggregation("priceRanges", ...) 추가
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.bool(b -> {
                    if (keyword != null && !keyword.isBlank()) {
                        b.must(m -> m.match(mm -> mm
                                .field("name")
                                .query(keyword)));
                    }
                    return b;
                }))
                .withMaxResults(0)
                // 브랜드별 개수 집계 (terms)
                .withAggregation("brands", Aggregation.of(a -> a
                        .terms(t -> t.field("brand"))))
                // 카테고리별 개수 집계 (terms)
                .withAggregation("categories", Aggregation.of(a -> a
                        .terms(t -> t.field("category"))))
                // 가격대별 개수 집계 (range)
                .withAggregation("priceRanges", Aggregation.of(a -> a
                        .range(r -> r
                                .field("price")
                                .ranges(range -> range.key("0-50000").to(50000.0))
                                .ranges(range -> range.key("50000-100000").from(50000.0).to(100000.0))
                                .ranges(range -> range.key("100000+").from(100000.0)))))
                .build();
        // TODO 2단계: operations.search(query, ProductDocument.class) 실행
        SearchHits<ProductDocument> hits = operations.search(query, ProductDocument.class);

        // TODO 3단계: hits.getAggregations() 결과 가져오기
        // - null 체크
        AggregationsContainer<?> aggregationsContainer = hits.getAggregations();
        if (aggregationsContainer == null) {
            return new ProductFilterAggregationResponse(List.of(), List.of(), List.of());
        }

        // TODO 4단계: brands 집계 결과 변환
        // - aggregationsContainer.aggregations().get("brands")
        // - .sterms().buckets().array().stream()
        // - .map((StringTermsBucket bucket) -> new ProductFilterBucketResponse(...))
        ElasticsearchAggregations esAggs = (ElasticsearchAggregations) aggregationsContainer;

        List<ProductFilterBucketResponse> brands = esAggs.get("brands")
                .aggregation().getAggregate().sterms()
                .buckets().array().stream()
                .map((StringTermsBucket bucket) -> new ProductFilterBucketResponse(
                        bucket.key().stringValue(),
                        bucket.docCount()))
                .toList();


        // TODO 5단계: categories 집계 결과 변환
        // - brands와 동일한 방식
        List<ProductFilterBucketResponse> categories = esAggs.get("categories")
                .aggregation().getAggregate().sterms()
                .buckets().array().stream()
                .map((StringTermsBucket bucket) -> new ProductFilterBucketResponse(
                        bucket.key().stringValue(),
                        bucket.docCount()))
                .toList();

        // TODO 6단계: priceRanges 집계 결과 변환
        // - .range() 사용 (terms 대신)
        List<ProductFilterBucketResponse> priceRanges = esAggs.get("priceRanges")
                .aggregation().getAggregate().range()
                .buckets().array().stream()
                .map((RangeBucket bucket) -> new ProductFilterBucketResponse(
                        bucket.key(),
                        bucket.docCount()))
                .toList();

        // TODO 7단계: 최종 응답 반환
        return new ProductFilterAggregationResponse(brands, categories, priceRanges);
    }

    @Override
    public ProductSuggestResponse suggestProducts(String keyword, int size) {
        if(keyword == null || keyword.isBlank()) {
            return new ProductSuggestResponse(List.of());
        }
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.matchPhrasePrefix(m-> m
                        .field("name")
                        .query(keyword)))
                .withPageable(PageRequest.of(0, size))
                .build();


        SearchHits<ProductDocument> hits = operations.search(query, ProductDocument.class);

        List<ProductSuggestItemResponse> items = hits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(doc -> new ProductSuggestItemResponse(
                        doc.getId(),
                        doc.getName(),
                        doc.getBrand()
                ))
                .toList();

        return new ProductSuggestResponse(items);
    }
}
