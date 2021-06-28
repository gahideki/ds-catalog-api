package com.dscatalog.repository;

import com.dscatalog.model.Category;
import com.dscatalog.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT prod FROM Product prod INNER JOIN prod.categories cats WHERE (:category IS NULL OR :category IN cats) " +
            "AND (LOWER(prod.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<Product> search(Pageable pageable, Category category, String name);

    @Query("SELECT prod FROM Product prod JOIN FETCH prod.categories WHERE prod IN :products")
    List<Product> findProductsCategories(List<Product> products);

}
