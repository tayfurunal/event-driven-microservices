package com.tayfurunal.basketservice.service.basketproductunit;

import com.tayfurunal.basketservice.domain.BasketProduct;
import com.tayfurunal.basketservice.domain.enums.BasketStatusName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BasketProductServiceTest {

    @InjectMocks
    private BasketProductService basketProductService;

    @Mock
    private BasketProductRepository basketProductRepository;

    @Test
    public void it_should_update_price_by_product_external_id() {
        // Given
        final BasketProduct basketProduct1 = BasketProduct.builder()
                .price(BigDecimal.valueOf(11))
                .build();
        final BasketProduct basketProduct2 = BasketProduct.builder()
                .price(BigDecimal.valueOf(23))
                .build();
        final List<BasketProduct> basketProducts = List.of(basketProduct1, basketProduct2);
        when(basketProductRepository.findAllByProduct_ExternalIdAndBasket_StatusId("33", BasketStatusName.ACTIVE.getId())).thenReturn(basketProducts);

        // When
        basketProductService.updatePriceByProductExternalId(33, BigDecimal.valueOf(43));

        // Then
        verify(basketProductRepository).findAllByProduct_ExternalIdAndBasket_StatusId("33", BasketStatusName.ACTIVE.getId());
        verify(basketProductRepository).saveAll(basketProducts);
        assertThat(basketProduct1.getPrice()).isEqualTo(BigDecimal.valueOf(43));
        assertThat(basketProduct2.getPrice()).isEqualTo(BigDecimal.valueOf(43));
        verifyNoMoreInteractions(basketProductRepository);
    }
}