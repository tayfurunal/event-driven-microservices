package com.tayfurunal.basketservice.listener;

import com.tayfurunal.basketservice.configuration.mq.queue.ProductPriceUpdateQueueConfiguration;
import com.tayfurunal.basketservice.model.ProductPriceUpdateDto;
import com.tayfurunal.basketservice.service.basketproductunit.BasketProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class ProductPriceUpdateListener {

    private final BasketProductService basketProductService;

    @RabbitListener(queues = ProductPriceUpdateQueueConfiguration.PRODUCT_PRICE_UPDATE_QUEUE, errorHandler = "defaultRabbitListenerErrorHandler")
    public void updateProductPrice(ProductPriceUpdateDto productPriceUpdateDto) {
        log.info("Read productPriceUpdateDto from the queue: {}", productPriceUpdateDto);
        basketProductService.updatePriceByProductExternalId(productPriceUpdateDto.getProductId(), productPriceUpdateDto.getPrice());
    }
}
