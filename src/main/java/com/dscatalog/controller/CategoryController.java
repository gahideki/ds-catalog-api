package com.dscatalog.controller;

import com.dscatalog.dto.CategoryDTO;
import com.dscatalog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        List<CategoryDTO> categories = service.findAll();
        if (categories.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
        CategoryDTO categoryDTO = service.findById(id);
        return ResponseEntity.ok(categoryDTO);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO categoryInput) {
        CategoryDTO categoryDTO = service.update(id, categoryInput);
        return ResponseEntity.ok(categoryDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
