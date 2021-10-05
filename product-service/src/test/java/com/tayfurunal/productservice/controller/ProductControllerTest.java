package com.tayfurunal.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tayfurunal.productservice.model.request.ProductUpdateRequest;
import com.tayfurunal.productservice.service.product.ProductService;
import com.tayfurunal.productservice.testconfigurations.MessageSourceTestConfiguration;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@ContextConfiguration(classes = MessageSourceTestConfiguration.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    public void it_should_update_product() throws Exception {
        // Given
        final ProductUpdateRequest productUpdateRequest = ProductUpdateRequest.builder()
                .price(BigDecimal.valueOf(33))
                .name("new package")
                .build();

        // When
        final ResultActions resultActions = mockMvc.perform(put("/products/45")
                                                                    .contentType(MediaType.APPLICATION_JSON)
                                                                    .content(objectMapper.writeValueAsString(productUpdateRequest)));

        // Then
        ArgumentCaptor<ProductUpdateRequest> argumentCaptor = ArgumentCaptor.forClass(ProductUpdateRequest.class);
        resultActions.andExpect(status().isAccepted());
        verify(productService).update(eq(45), argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getName()).isEqualTo("new package");
        assertThat(argumentCaptor.getValue().getPrice()).isEqualTo(BigDecimal.valueOf(33));
    }

    @Test
    public void it_should_throw_exception_when_name_is_null() throws Exception {
        // Given
        final ProductUpdateRequest productUpdateRequest = ProductUpdateRequest.builder().build();

        // When
        final ResultActions resultActions = mockMvc.perform(put("/products/45")
                                                                    .contentType(MediaType.APPLICATION_JSON)
                                                                    .content(objectMapper.writeValueAsString(productUpdateRequest)));
        // Then
        verifyNoInteractions(productService);
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception", is("MethodArgumentNotValidException")))
                .andExpect(jsonPath("$.errors", containsInAnyOrder("Product name cannot be blank.", "Product price cannot be null.")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }
}