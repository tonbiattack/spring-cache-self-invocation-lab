package com.example.cachelab.catalog;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

@Component
public class ProductRepository {
    private final AtomicInteger readCount = new AtomicInteger();

    public Product findById(String id) {
        readCount.incrementAndGet();
        return new Product(id, "Notebook");
    }

    public int readCount() {
        return readCount.get();
    }
}
