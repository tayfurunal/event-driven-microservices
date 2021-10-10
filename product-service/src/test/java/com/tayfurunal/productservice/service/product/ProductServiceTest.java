package com.tayfurunal.productservice.service.product;

import com.tayfurunal.productservice.domain.Product;
import com.tayfurunal.productservice.exception.ProductServiceNotFoundException;
import com.tayfurunal.productservice.model.ProductPriceUpdateDto;
import com.tayfurunal.productservice.model.request.ProductUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Test
    public void it_should_update() {
        // Given
        final ProductUpdateRequest productUpdateRequest = ProductUpdateRequest.builder()
                .name("new name")
                .price(BigDecimal.valueOf(45.5))
                .build();
        final Product product = Product.builder().price(BigDecimal.TEN).build();
        when(productRepository.findById(54)).thenReturn(Optional.of(product));
        ArgumentCaptor<ProductPriceUpdateDto> priceUpdateDtoArgumentCaptor = ArgumentCaptor.forClass(ProductPriceUpdateDto.class);

        // When
        productService.update(54, productUpdateRequest);

        // Then
        verify(productRepository).save(product);
        verify(productRepository).findById(54);
        verify(rabbitTemplate).convertAndSend(eq("basket.service.product.price.update"), eq(""), priceUpdateDtoArgumentCaptor.capture());
        assertThat(product.getName()).isEqualTo("new name");
        assertThat(product.getPrice()).isEqualTo(BigDecimal.valueOf(45.5));
    }

    @Test
    public void it_should_not_update_when_product_not_found() {
        // Given
        final ProductUpdateRequest productUpdateRequest = ProductUpdateRequest.builder().build();
        when(productRepository.findById(54)).thenReturn(Optional.empty());

        // When
        Throwable throwable = catchThrowable(() -> productService.update(54, productUpdateRequest));

        // Then
        verify(productRepository).findById(54);
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(rabbitTemplate);
        assertThat(throwable).isInstanceOf(ProductServiceNotFoundException.class);
        ProductServiceNotFoundException productServiceNotFoundException = (ProductServiceNotFoundException) throwable;
        assertThat(productServiceNotFoundException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(productServiceNotFoundException.getKey()).isEqualTo("product.not.found");
    }
}