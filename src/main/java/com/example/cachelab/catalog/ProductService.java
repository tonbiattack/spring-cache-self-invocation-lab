package com.example.cachelab.catalog;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product get(String id) {
        return load(id);
    }

    @Cacheable(cacheNames = "products", key = "#id")
    Product load(String id) {
        return repository.findById(id);
    }
}
