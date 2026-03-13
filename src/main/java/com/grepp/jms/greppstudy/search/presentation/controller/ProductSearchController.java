package com.grepp.jms.greppstudy.search.presentation.controller;

import com.grepp.jms.greppstudy.search.application.SearchService;
import com.grepp.jms.greppstudy.search.infrastructure.dto.ProductDocument;
import com.grepp.jms.greppstudy.search.presentation.dto.request.IndexConfigRequest;
import com.grepp.jms.greppstudy.search.presentation.dto.request.ProductIndexRequest;
import com.grepp.jms.greppstudy.search.presentation.dto.response.IndexStatusResponse;
import com.grepp.jms.greppstudy.search.presentation.dto.response.IndexUpdateResponse;
import com.grepp.jms.greppstudy.search.presentation.dto.response.ProductSearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "검색")
// 검색/색인 관련 API 엔드포인트를 노출하는 컨트롤러
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class ProductSearchController {
    private final SearchService searchService;

    @Operation(summary = "상품 인덱스 상태 조회", description = "인덱스 존재 여부, 설정, 매핑 정보를 반환합니다. ")
    @GetMapping("/products/index")
    public IndexStatusResponse getProductIndexStatus() {return searchService.getProductIndexStatus();}

    @Operation(
            summary = "상품 인덱스 설정/매핑 갱신",
            description = "인덱스가 없으면 생성하고, 있으면 레플리카/매핑을 업데이트합니다. 샤드 수 변경은 기존 인덱스에 적용되지 않습니다."
    )
    @PutMapping("/products/index")
    public IndexUpdateResponse updateIndex(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "인덱스 설정",
                    required = true,
                    content = @Content(examples = @ExampleObject(value = "{\n  \"numberOfShards\": 3,\n  \"numberOfReplicas\": 0\n}"))
            )
            IndexConfigRequest request
    ) {
        return searchService.applyProductIndexConfig(request);
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDocument> indexProduct(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "상품 색인 요청",
                    required = true,
                    content = @Content(examples = @ExampleObject(value = "{\n  \"name\": \"남자 셔츠\",\n  \"brand\": \"SHOP\",\n  \"category\": \"shirts\",\n  \"price\": 59000\n}"))
            ) ProductIndexRequest request
    ) {
        ProductDocument saved = searchService.indexProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/products")
    public ProductSearchResponse searchProducts(
            @Parameter(description = "검색 키워드", example = "남자 신발")
            @RequestParam(required = false) String keyword,
            @Parameter(description = "카테고리 필터", example = "shoes")
            @RequestParam(required = false) String category,
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        return searchService.searchProducts(keyword, category, pageable);
    }

}
