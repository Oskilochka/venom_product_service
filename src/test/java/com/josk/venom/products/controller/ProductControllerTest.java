package com.josk.venom.products.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.josk.venom.products.exception.ProductNotFoundException;
import com.josk.venom.products.model.Product;
import com.josk.venom.products.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductServiceImpl productService;
    private Product testProduct;

    private final static String PRODUCT_NAME = "Test Product";
    private final static Integer PRODUCT_PRICE = 100;

    @BeforeEach
    public void setup() {
        testProduct = Product.builder().name(PRODUCT_NAME).price(PRODUCT_PRICE).build();
    }

    @Test
    void getAllProducts_ShouldReturnListOfProducts() throws Exception {
        List<Product> productList = Collections.singletonList(testProduct);
        when(productService.getAllProducts()).thenReturn(productList);

        mockMvc.perform(get("/api/v1/products")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value(PRODUCT_NAME))
                .andExpect(jsonPath("$[0].price").value(PRODUCT_PRICE));
    }

    @Test
    void getProductById_ShouldReturnProductById() throws Exception {
        testProduct.setId(1L);
        when(productService.getProductById(1L)).thenReturn(testProduct);

        mockMvc.perform(get("/api/v1/products/{id}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("name").value(PRODUCT_NAME))
                .andExpect(jsonPath("price").value(PRODUCT_PRICE));
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        when(productService.createProduct(any(Product.class))).thenReturn(testProduct);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(testProduct))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("name").value(PRODUCT_NAME))
                .andExpect(jsonPath("price").value(PRODUCT_PRICE));
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProduct() throws Exception {
        Product updatedProduct = Product.builder().id(1L).name("Updated product").price(200).build();
        when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/v1/products/{id}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedProduct))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("name").value("Updated product"))
                .andExpect(jsonPath("price").value(200));
    }

    @Test
    void deleteProduct_ShouldReturnNoContent() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/v1/products/{id}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProduct_ShouldReturnNotFound_WhenProductDoesNotExist() throws Exception {
        doThrow(new ProductNotFoundException("Product not found")).when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/v1/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
