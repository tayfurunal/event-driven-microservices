package com.tayfurunal.productservice.service.product;

import com.tayfurunal.productservice.domain.Product;
import com.tayfurunal.productservice.exception.ProductServiceNotFoundException;
import com.tayfurunal.productservice.model.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void update(Integer id, ProductUpdateRequest productUpdateRequest) {
        final Product product = findById(id);
        product.setName(productUpdateRequest.getName());
        product.setPrice(productUpdateRequest.getPrice());
        productRepository.save(product);
        log.info("Product updated: {}", product);
    }

    private Product findById(Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductServiceNotFoundException("product.not.found"));
    }
}
