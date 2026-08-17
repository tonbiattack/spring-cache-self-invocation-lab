package com.example.cachelab.catalog;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ProductCacheService {
    private final ProductRepository repository;

    public ProductCacheService(ProductRepository repository) {
        this.repository = repository;
    }

    @Cacheable(cacheNames = "products", key = "#id")
    public Product load(String id) {
        return repository.findById(id);
    }
}
