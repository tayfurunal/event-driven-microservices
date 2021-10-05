package com.tayfurunal.productservice.controller;

import com.tayfurunal.productservice.model.request.ProductUpdateRequest;
import com.tayfurunal.productservice.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.ACCEPTED;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PutMapping("/{id}")
    @ResponseStatus(ACCEPTED)
    public void update(@PathVariable Integer id, @RequestBody @Valid ProductUpdateRequest productUpdateRequest) {
        log.info("productUpdateRequest: {}", productUpdateRequest);
        productService.update(id, productUpdateRequest);
    }
}
