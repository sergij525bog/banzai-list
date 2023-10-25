package org.oldman.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.oldman.entities.enums.Priority;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "lists_with_products")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class ListWithProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "list_id")
    private ProductList productList;

    @PositiveOrZero
    private int count;

    private LocalDateTime endDate = null;
    private Priority priority = Priority.LOW;
    @NotNull
    @Column(nullable = false)
    private boolean retrieved = false;

    @Positive
    private double weight;

    @NotNull
    @Column(nullable = false)
    private boolean required = false;

    @Override
    public String toString() {
        return "Item{id=" +
                id +
                ", name='" +
                product.getName() +
                '\'' +
                ", count=" +
                count +
                ", retrieved=" +
                retrieved +
                ", weight=" +
                weight +
                ", required=" +
                required +
                '}';
    }
}
