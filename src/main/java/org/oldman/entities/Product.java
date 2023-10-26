package org.oldman.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.oldman.entities.enums.ProductCategory;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "products")
public class Product implements Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @NotBlank(message = "name is required")
    @Column(nullable = false)
    private String name;

    private ProductCategory productCategory;

    @OneToMany(mappedBy = "product")
    Set<ListWithProduct> listWithProducts = new HashSet<>();


}
