package com.tayfurunal.basketservice.listener;

import com.tayfurunal.basketservice.model.ProductPriceUpdateDto;
import com.tayfurunal.basketservice.service.basketproductunit.BasketProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductPriceUpdateListenerTest {

    @InjectMocks
    private ProductPriceUpdateListener productPriceUpdateListener;

    @Mock
    private BasketProductService basketProductService;

    @Test
    public void it_should_update_product_price() {
        // Given
        final ProductPriceUpdateDto productPriceUpdateDto = ProductPriceUpdateDto.builder()
                .price(BigDecimal.valueOf(43))
                .productId(33)
                .build();

        // When
        productPriceUpdateListener.updateProductPrice(productPriceUpdateDto);

        // Then
        verify(basketProductService).updatePriceByProductExternalId(33, BigDecimal.valueOf(43));
    }
}