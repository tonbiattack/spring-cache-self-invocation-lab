package com.example.cachelab;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.cachelab.catalog.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CacheLabApplicationTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProductRepository repository;

    @Test
    void repeated_reads_return_same_product_without_hitting_repository_twice() throws Exception {
        mockMvc.perform(get("/products/p-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("p-1")))
                .andExpect(jsonPath("$.name", is("Notebook")));

        mockMvc.perform(get("/products/p-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("p-1")))
                .andExpect(jsonPath("$.name", is("Notebook")));

        org.assertj.core.api.Assertions.assertThat(repository.readCount()).isEqualTo(1);
    }
}
