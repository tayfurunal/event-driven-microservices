package com.tayfurunal.productservice.service.product;

import com.tayfurunal.productservice.domain.Product;
import com.tayfurunal.productservice.exception.ProductServiceNotFoundException;
import com.tayfurunal.productservice.model.ProductPriceUpdateDto;
import com.tayfurunal.productservice.model.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    public static final String BASKET_SERVICE_PRODUCT_PRICE_UPDATE = "basket.service.product.price.update";
    private final ProductRepository productRepository;
    private final RabbitTemplate rabbitTemplate;

    public void update(Integer id, ProductUpdateRequest productUpdateRequest) {
        final Product product = findById(id);
        final BigDecimal oldPrice = product.getPrice();
        final BigDecimal newPrice = productUpdateRequest.getPrice();
        product.setName(productUpdateRequest.getName());
        product.setPrice(newPrice);
        productRepository.save(product);
        log.info("Product updated: {}", product);

        if (oldPrice.compareTo(newPrice) != 0) {
            enqueueProductPriceUpdate(id, newPrice);
        }
    }

    private void enqueueProductPriceUpdate(Integer productId, BigDecimal price) {
        final ProductPriceUpdateDto productPriceUpdateDto = ProductPriceUpdateDto.builder()
                .productId(productId)
                .price(price)
                .build();
        rabbitTemplate.convertAndSend(BASKET_SERVICE_PRODUCT_PRICE_UPDATE, "", productPriceUpdateDto);
        log.info("push productPriceUpdateDto to queue {} ", productPriceUpdateDto);
    }

    private Product findById(Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductServiceNotFoundException("product.not.found"));
    }
}
