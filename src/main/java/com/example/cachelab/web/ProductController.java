package com.example.cachelab.web;

import com.example.cachelab.catalog.Product;
import com.example.cachelab.catalog.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/products/{id}")
    Product get(@PathVariable String id) {
        return service.get(id);
    }
}
