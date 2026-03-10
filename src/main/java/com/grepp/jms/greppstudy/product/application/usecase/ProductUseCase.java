package com.grepp.jms.greppstudy.product.application.usecase;

import com.grepp.jms.greppstudy.product.domain.model.Product;
import com.grepp.jms.greppstudy.product.presentation.dto.request.CreateProductRequest;
import com.grepp.jms.greppstudy.product.presentation.dto.request.UpdateProductRequest;
import java.util.List;
import java.util.UUID;

public interface ProductUseCase {

    Product create(CreateProductRequest request, UUID actorId);

    Product getById(UUID productId);

    List<Product> getAll();

    Product update(UUID productId, UpdateProductRequest request, UUID actorId);

    void delete(UUID productId);
}
