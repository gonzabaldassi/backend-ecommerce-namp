package com.namp.ecommerce.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Subcategory")
public class Subcategory implements Serializable {
    @Id
    @Column(name = "idSubcategory")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "El nombre no puede estar vacio")
    private String name;

    @NotNull(message = "La descripcion no puede estar vacia")
    private String description;

    @NotNull(message = "La categoria no puede estar vacia")
    @ManyToOne
    @JoinColumn(name = "fk_category", referencedColumnName = "idCategory")
    private Category idCategory;
}
