package com.dscatalog.service;

import com.dscatalog.dto.CategoryDTO;
import com.dscatalog.model.Category;
import com.dscatalog.repository.CategoryRepository;
import com.dscatalog.service.exception.DatabaseException;
import com.dscatalog.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> categories = repository.findAll();
        return categories.stream().map(CategoryDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Category category = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO save(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category = repository.save(category);
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO categoryInput) {
        try {
            Category category = repository.getOne(id);
            category.setName(categoryInput.getName());
            return new CategoryDTO(category);
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
