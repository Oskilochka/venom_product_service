package com.josk.venom.service;

import com.josk.venom.exception.ProductNotFoundException;
import com.josk.venom.model.Product;
import com.josk.venom.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        return productRepository.findById(id)
                .map(existingCategory -> {
                            existingCategory.setName(product.getName());
                            existingCategory.setPrice(product.getPrice());
                            return productRepository.save(existingCategory);
                        }
                ).orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
