package com.dscatalog.service;

import com.dscatalog.repository.CategoryRepository;
import com.dscatalog.repository.ProductRepository;
import com.dscatalog.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    public void setup() {
        existingId = 1L;
        nonExistingId = 1000L;

        doNothing().when(repository).deleteById(existingId);
        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        assertDoesNotThrow(() -> {
            productService.delete(existingId);
        });

        verify(repository).deleteById(existingId);
        verify(repository, times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdNonExists() {
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(nonExistingId);
        });

        verify(repository).deleteById(nonExistingId);
        verify(repository, times(1)).deleteById(nonExistingId);
    }

}