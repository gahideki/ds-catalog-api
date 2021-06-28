package com.dscatalog.service;

import com.dscatalog.dto.ProductDTO;
import com.dscatalog.repository.ProductRepository;
import com.dscatalog.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@Transactional
public class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    @BeforeEach
    public void setup() {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists() {
        productService.delete(existingId);
        assertEquals(countTotalProducts - 1, productRepository.count());
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesExists() {
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(nonExistingId);
        });
    }

    @Test
    public void findAllPagedShouldReturnPageWhenPage0Size10() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ProductDTO> result = productService.findAllPaged(pageRequest, 0L, any());

        assertFalse(result.isEmpty());
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());
        assertEquals(countTotalProducts, result.getTotalElements());
    }

    @Test
    public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExist() {
        PageRequest pageRequest = PageRequest.of(50, 10);
        Page<ProductDTO> result = productService.findAllPaged(pageRequest, 0L, any());

        assertTrue(result.isEmpty());
    }

    @Test
    public void findAllPagedShouldReturnOrderedPageWhenSortByName() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));
        Page<ProductDTO> result = productService.findAllPaged(pageRequest, 0L, any());

        assertFalse(result.isEmpty());
        assertEquals("Macbook Pro", result.getContent().get(0).getName());
        assertEquals("PC Gamer", result.getContent().get(1).getName());
        assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }

}
