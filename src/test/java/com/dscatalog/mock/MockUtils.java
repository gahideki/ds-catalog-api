package com.dscatalog.mock;

import com.dscatalog.dto.ProductDTO;
import com.dscatalog.model.Category;
import com.dscatalog.model.Product;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MockUtils {

    public static Product createProduct() {
        Product product = new Product(1L, "Phone", "iPhone", new BigDecimal(800.0), "https://imgs.com/img.png", OffsetDateTime.now().now(), new HashSet<>());
        product.getCategories().add(new Category(1L, "Electronics", OffsetDateTime.now(), OffsetDateTime.now()));
        return product;
    }
    
    public static ProductDTO creaProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }

}
