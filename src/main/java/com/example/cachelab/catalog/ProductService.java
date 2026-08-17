package com.example.cachelab.catalog;

import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductCacheService cacheService;

    public ProductService(ProductCacheService cacheService) {
        this.cacheService = cacheService;
    }

    public Product get(String id) {
        return cacheService.load(id);
    }
}
