package com.tayfurunal.productservice.service.product;

import com.tayfurunal.productservice.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

interface ProductRepository extends JpaRepository<Product, Integer> {
}
