package com.josk.venom.repository;

import com.josk.venom.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Product product;

    @BeforeEach
    public void setupTestData(){
        productRepository.deleteAll();
        product = Product.builder().name("Test Product").price(200).build();
    }

    @Test
    void findById_ShouldReturnProduct() {
        entityManager.persist(product);
        entityManager.flush();

        Product foundProduct = productRepository.findById(product.getId()).orElse(null);
        assertNotNull(foundProduct);
        assertEquals("Test Product", foundProduct.getName());
    }

    @Test
    void findAll_ShouldReturnListOfProducts() {
        Product product1 = Product.builder().name("Product 1").price(100).build();
        Product product2 = Product.builder().name("Product 2").price(200).build();
        Product product3 = Product.builder().name("Product 3").price(300).build();

        entityManager.persist(product1);
        entityManager.persist(product2);
        entityManager.persist(product3);
        entityManager.flush();

        List<Product> products = productRepository.findAll();

        assertNotNull(products);
        assertEquals(3, products.size(), "Should return 3 products");
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Product 1")));
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Product 2")));
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Product 3")));
    }

    @Test
    void save_ShouldReturnProduct() {
        Product savedProduct = productRepository.save(product);

        assertNotNull(savedProduct);
        assertEquals(200, savedProduct.getPrice());
        assertEquals("Test Product", savedProduct.getName());
    }

    @Test
    void delete_ShouldDeleteProduct() {
        Product savedProduct = entityManager.persist(product);
        entityManager.flush();

        productRepository.deleteById(savedProduct.getId());
        entityManager.flush();

        assertFalse(productRepository.findById(savedProduct.getId()).isPresent(),
                "Product should be deleted and not found.");
    }
}
