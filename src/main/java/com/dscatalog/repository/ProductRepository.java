package com.dscatalog.repository;

import com.dscatalog.model.Category;
import com.dscatalog.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT prod FROM Product prod INNER JOIN prod.categories cats WHERE (:category IS NULL OR :category IN cats) ")
    Page<Product> search(Pageable pageable, Category category);

}
