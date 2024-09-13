package com.namp.ecommerce.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Category")
public class Category implements Serializable {
    @Id
    @Column(name = "idCategory")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCategory;

    @NotEmpty(message = "El nombre no debe estar vacio")
    @Pattern(regexp = "^(?!\s*$)[a-zA-Z\s]+$",message = "El nombre debe contener solo caracteres alfabeticos")
    private String name;

    @NotNull(message = "La descripcion no debe estar vacia")
    private String description;

    
    @OneToMany(mappedBy = "idCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subcategory> subCategories;
}
