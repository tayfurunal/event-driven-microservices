package com.tayfurunal.basketservice.service.basketproductunit;

import com.tayfurunal.basketservice.domain.BasketProduct;
import com.tayfurunal.basketservice.domain.enums.BasketStatusName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketProductService {

    private final BasketProductRepository basketProductRepository;

    public void updatePriceByProductExternalId(Integer productId, BigDecimal price) {
        final List<BasketProduct> basketProducts = basketProductRepository.findAllByProduct_ExternalIdAndBasket_StatusId(String.valueOf(productId),
                                                                                                                         BasketStatusName.ACTIVE.getId());
        basketProducts.forEach(it -> it.setPrice(price));
        basketProductRepository.saveAll(basketProducts);
    }
}
