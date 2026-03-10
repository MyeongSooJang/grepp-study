package com.grepp.jms.greppstudy.product.application.service;

import com.grepp.jms.greppstudy.product.application.acl.SellerAcl;
import com.grepp.jms.greppstudy.product.application.acl.SellerIdentity;
import com.grepp.jms.greppstudy.product.application.exception.ProductNotFoundException;
import com.grepp.jms.greppstudy.product.application.usecase.ProductUseCase;
import com.grepp.jms.greppstudy.product.domain.model.Product;
import com.grepp.jms.greppstudy.product.domain.repository.ProductRepository;
import com.grepp.jms.greppstudy.product.presentation.dto.request.CreateProductRequest;
import com.grepp.jms.greppstudy.product.presentation.dto.request.UpdateProductRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductApplicationService implements ProductUseCase {

    private final SellerAcl sellerAcl;
    private final ProductRepository productRepository;

    public ProductApplicationService(SellerAcl sellerAcl,
                                     ProductRepository productRepository) {
        this.sellerAcl = sellerAcl;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Product create(CreateProductRequest request, UUID actorId) {
        SellerIdentity seller = sellerAcl.loadActiveSeller(request.sellerId());
        Product product = Product.create(
                seller.id(),
                request.name(),
                request.description(),
                request.price(),
                request.stock(),
                request.status(),
                actorId
        );
        return productRepository.save(product);
    }

    @Override
    public Product getById(UUID productId) {
        return findByIdOrThrow(productId);
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    @Transactional
    public Product update(UUID productId, UpdateProductRequest request, UUID actorId) {
        Product product = findByIdOrThrow(productId);
        product.update(
                request.name(),
                request.description(),
                request.price(),
                request.stock(),
                request.status(),
                actorId
        );
        return product;
    }

    @Override
    @Transactional
    public void delete(UUID productId) {
        Product product = findByIdOrThrow(productId);
        productRepository.delete(product);
    }

    private Product findByIdOrThrow(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}
