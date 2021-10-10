package com.tayfurunal.basketservice.service.basketproductunit;

import com.tayfurunal.basketservice.domain.Basket;
import com.tayfurunal.basketservice.domain.BasketProduct;
import com.tayfurunal.basketservice.domain.Product;
import com.tayfurunal.basketservice.domain.enums.BasketStatusName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class BasketProductRepositoryTest {

    @Autowired
    private BasketProductRepository basketProductRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void it_should_get_all_basket_product_by_external_id_and_basket_status() {
        // Given
        final Basket basket1 = Basket.builder().userId(2323).statusId(1).build();
        final Basket basket2 = Basket.builder().userId(2323).statusId(2).build();
        testEntityManager.persistAndFlush(basket1);
        testEntityManager.persistAndFlush(basket2);

        final Product product1 = Product.builder().externalId("3").name("paper").build();
        final Product product2 = Product.builder().externalId("4").name("paper").build();
        testEntityManager.persistAndFlush(product1);
        testEntityManager.persistAndFlush(product2);

        final BasketProduct basketProduct1 = BasketProduct.builder()
                .price(BigDecimal.ONE)
                .quantity(1)
                .product(product1)
                .basketId(basket1.getId())
                .build();

        final BasketProduct basketProduct2 = BasketProduct.builder()
                .price(BigDecimal.ONE)
                .quantity(1)
                .product(product1)
                .basketId(basket2.getId())
                .build();

        final BasketProduct basketProduct3 = BasketProduct.builder()
                .price(BigDecimal.ONE)
                .quantity(1)
                .product(product2)
                .basketId(basket1.getId())
                .build();

        basketProductRepository.save(basketProduct1);
        basketProductRepository.save(basketProduct2);
        basketProductRepository.save(basketProduct3);

        // When
        final List<BasketProduct> basketProducts = basketProductRepository.findAllByProduct_ExternalIdAndBasket_StatusId("3",
                                                                                                                         BasketStatusName.ACTIVE.getId());

        // Then
        assertThat(basketProducts).containsExactly(basketProduct1);
    }
}