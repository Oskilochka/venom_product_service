package com.josk.venom.products.service;

import com.josk.venom.products.exception.ProductNotFoundException;
import com.josk.venom.products.model.Product;
import com.josk.venom.products.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        Product product1 = new Product(1L, "Test Product 1", 100.0);
        Product product2 = new Product(2L, "Test Product 2", 50.0);
        List<Product> expectedProducts = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(expectedProducts);

        List<Product> result = productService.getAllProducts();

        assertEquals(expectedProducts.size(), result.size());
        assertEquals(expectedProducts, result);
        assertEquals(2, result.size());
        assertEquals("Test Product 1", result.get(0).getName());
        assertEquals(100.0, result.get(0).getPrice());

        verify(productRepository).findAll();
    }

    @Test
    void getAllProducts_ShouldReturnEmptyList() {
        when(productRepository.findAll()).thenReturn(List.of());
        List<Product> result = productService.getAllProducts();
        assertEquals(0, result.size());
    }

    @Test
    void getProductById_ShouldReturnProduct() {
        Long productId = 1L;
        Product existingProduct = new Product(productId, "Test Product", 100.0);
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        Product result = productService.getProductById(productId);

        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("Test Product", result.getName());
        assertEquals(100.0, result.getPrice());

        verify(productRepository).findById(productId);
    }

    @Test
    void getProductById_ShouldThrowNotFoundException() {
        Long invalidProductId = 999L;
        when(productRepository.findById(invalidProductId)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> productService.getProductById(invalidProductId));

        assertEquals("Product not found with id: " + invalidProductId, exception.getMessage());
        verify(productRepository).findById(invalidProductId);
    }

    @Test
    void createProduct_ShouldReturnSavedProduct() {
        Product productToSave = new Product(null, "Test Product", 100.0);
        Product savedProduct = new Product(1L, "Test Product", 100.0);

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Product result = productService.createProduct(productToSave);

        assertNotNull(result);
        assertEquals(savedProduct.getId(), result.getId());
        assertEquals("Test Product", result.getName());
        assertEquals(100.0, result.getPrice());

        verify(productRepository).save(productToSave);
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProduct_WhenProductExists() {
        Long validProductId = 1L;
        Product existingProduct = new Product(validProductId, "Old Product", 50.0);
        Product updatedProduct = new Product(validProductId, "Updated Product", 75.0);

        when(productRepository.findById(validProductId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        Product result = productService.updateProduct(validProductId, updatedProduct);

        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        assertEquals(75.0, result.getPrice());

        verify(productRepository).save(existingProduct);
    }

    @Test
    void updateProduct_ShouldThrowNotFoundException() {
        Long invalidProductId = 999L;
        Product updatedProduct = new Product(invalidProductId, "Updated Product", 75.0);

        when(productRepository.findById(invalidProductId)).thenReturn(java.util.Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(invalidProductId, updatedProduct));

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_ShouldDeleteProductById() {
        Long validProductId = 1L;
        when(productRepository.existsById(validProductId)).thenReturn(true);

        productService.deleteProduct(validProductId);

        verify(productRepository).deleteById(validProductId);
    }

    @Test
    void deleteProduct_ShouldThrowNotFoundException() {
        Long invalidProductId = 999L;
        when(productRepository.existsById(invalidProductId)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(invalidProductId));

        verify(productRepository, never()).deleteById(invalidProductId);
    }
}
