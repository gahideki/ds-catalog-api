package com.dscatalog.service;

import com.dscatalog.dto.ProductDTO;
import com.dscatalog.model.Product;
import com.dscatalog.repository.ProductRepository;
import com.dscatalog.service.exception.DatabaseException;
import com.dscatalog.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
        Page<Product> products = repository.findAll(pageRequest);
        return products.map(p -> new ProductDTO(p, p.getCategories()));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return new ProductDTO(product, product.getCategories());
    }

    @Transactional
    public ProductDTO save(ProductDTO productDTO) {
        Product product = new Product();
        // Product.setName(productDTO.getName());
        product = repository.save(product);
        return new ProductDTO(product);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productInput) {
        try {
            Product product = repository.getOne(id);
            // Product.setName(productInput.getName());
            return new ProductDTO(product);
        } catch (EntityNotFoundException ex) {
            throw new ResourceNotFoundException(String.format("Id %d not found", id));
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException(String.format("Id %d not found", id));
        } catch (DataIntegrityViolationException ex) {
            throw new DatabaseException("Integrity violation");
        }
    }

}
