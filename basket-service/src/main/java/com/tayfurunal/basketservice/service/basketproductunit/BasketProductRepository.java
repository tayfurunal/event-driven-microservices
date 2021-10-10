package com.tayfurunal.basketservice.service.basketproductunit;

import com.tayfurunal.basketservice.domain.BasketProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BasketProductRepository extends JpaRepository<BasketProduct, Integer> {

    List<BasketProduct> findAllByProduct_ExternalIdAndBasket_StatusId(String productExternalId, Integer statusId);
}
