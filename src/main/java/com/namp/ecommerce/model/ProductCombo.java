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
@Table(name = "ProductCombo")
public class ProductCombo implements Serializable{
    @Id
    @Column(name = "idProductCombo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idProductCombo;  

    @NotNull(message = "La cantidad no debe de estar vacia")
    @Min(value = 0, message = "El valor debe ser un número positivo")
    private int cant;

    @ManyToOne
    @JoinColumn(name = "fk_product", referencedColumnName = "idProduct")
    private Category idProduct;

    @ManyToOne
    @JoinColumn(name = "fk_combo", referencedColumnName = "idCombo")
    private Category idCombo;

}


