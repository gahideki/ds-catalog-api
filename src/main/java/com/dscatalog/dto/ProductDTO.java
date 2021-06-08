package com.dscatalog.dto;

import com.dscatalog.model.Category;
import com.dscatalog.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long id;

    @Size(min = 5, max = 60, message = "Deve ter entre 5 e 60 caracteres")
    @NotBlank(message = "Campo obrigatório")
    private String name;

    private String description;

    @Positive(message = "Preço deve ser um valor positivo")
    private BigDecimal price;

    private String imgUrl;

    @PastOrPresent(message = "Data do produto não poder futura")
    private OffsetDateTime date;

    private Set<CategoryDTO> categories = new HashSet<>();

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.imgUrl = product.getImgUrl();
        this.date = product.getDate();
    }

    public ProductDTO(Product product, Set<Category> categories) {
        this(product);
        this.categories = categories.stream().map(CategoryDTO::new).collect(Collectors.toSet());
    }

}
