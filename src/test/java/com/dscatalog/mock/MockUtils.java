package com.dscatalog.mock;

import com.dscatalog.dto.ProductDTO;
import com.dscatalog.model.Category;
import com.dscatalog.model.Product;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;

public class MockUtils {

    public static Product createProduct() {
        Product product = new Product(1L, "Phone", "iPhone", new BigDecimal(800.0), "https://imgs.com/img.png", OffsetDateTime.now().now(), new HashSet<>());
        product.getCategories().add(createCategory());
        return product;
    }
    
    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }

    public static Category createCategory() {
        Category category = new Category(1L, "Electronics", OffsetDateTime.now(), OffsetDateTime.now());
        return category;
    }

}
