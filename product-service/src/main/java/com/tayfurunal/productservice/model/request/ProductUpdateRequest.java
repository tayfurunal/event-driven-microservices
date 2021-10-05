package com.tayfurunal.productservice.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequest {

    @NotBlank(message = "productUpdateRequest.name.notBlank")
    private String name;

    @NotNull(message = "productUpdateRequest.price.notNull")
    @Positive(message = "productUpdateRequest.price.positive")
    private BigDecimal price;
}
