package com.dscatalog.service;

import com.dscatalog.dto.ProductDTO;
import com.dscatalog.model.Category;
import com.dscatalog.model.Product;
import com.dscatalog.repository.CategoryRepository;
import com.dscatalog.repository.ProductRepository;
import com.dscatalog.service.exception.DatabaseException;
import com.dscatalog.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Pageable pageable, Long categoryId, String name) {
        Category category = (categoryId == 0) ? null : categoryRepository.getOne(categoryId);
        Page<Product> products = productRepository.search(pageable, category, name);
        productRepository.findProductsCategories(products.stream().collect(Collectors.toList()));
        return products.map(p -> new ProductDTO(p, p.getCategories()));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return new ProductDTO(product, product.getCategories());
    }

    @Transactional
    public ProductDTO save(ProductDTO productDTO) {
        Product product = new Product();
        copyDtoToEntity(productDTO, product);
        product = productRepository.save(product);
        return new ProductDTO(product);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productInput) {
        try {
            Product product = productRepository.getOne(id);
            copyDtoToEntity(productInput, product);
            return new ProductDTO(product);
        } catch (EntityNotFoundException ex) {
            throw new ResourceNotFoundException(String.format("Id %d not found", id));
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException(String.format("Id %d not found", id));
        } catch (DataIntegrityViolationException ex) {
            throw new DatabaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(ProductDTO productDTO, Product product) {
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setDate(productDTO.getDate());
        product.setImgUrl(productDTO.getImgUrl());
        product.setPrice(productDTO.getPrice());
        product.getCategories().clear();

        productDTO.getCategories().forEach(c -> {
            Category category = categoryRepository.getOne(c.getId());
            product.getCategories().add(category);
        });
    }

}
