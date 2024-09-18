package com.namp.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Combo")


public class Combo implements Serializable {
    @Id
    @Column(name = "idCombo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCombo;
    
    @NotEmpty(message = "El nombre no debe estar vacio")
    @Pattern(regexp = "^(?!\s*$)[a-zA-Z\s]+$",message = "El nombre debe contener solo caracteres alfabeticos")
    private String name;

    @NotNull(message = "La descripcion no debe estar vacia")
    private String description;

    @NotNull(message = "El precio no debe estar vacio")
    @Min(value = 0, message = "El valor debe ser un número positivo")
    private double price;

    @OneToMany(mappedBy = "idCombo")
    private List<ProductCombo> ProductCombo = new ArrayList<>();
}


