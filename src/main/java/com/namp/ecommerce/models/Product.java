package com.namp.ecommerce.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
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
@Table(name="product")
public class Product implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProduct")
    private long idProduct;

    @NotNull(message = "El nombre no debe estar vacio")
    private String name;

    @NotNull(message = "La descripcion no debe estar vacio")
    private String description;

    @NotNull(message = "El precio no debe estar vacio")
    @Min(value = 0, message = "El valor debe ser un número positivo")
    private double price;

    @NotNull(message = "El stock no debe estar vacio")
    @Min(value = 0, message = "El valor debe ser un número positivo")
    private int stock;

    //private String img;
    //Falta promocion

    @NotNull
    @ManyToOne
    @JoinColumn(name = "fk_subcategory", referencedColumnName = "idSubcategory")
    private Subcategory idSubcategory;
}
