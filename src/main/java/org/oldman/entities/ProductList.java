package org.oldman.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "product_lists")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ProductList {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "name is required")
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "productList")
    Set<ListWithProduct> listWithProducts = new HashSet<>();

//    public ItemList(String name) {
//        setName(name);
//        tasks = new ArrayList<>();
//    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank!");
        }
        this.name = name;
    }

    @JsonIgnore
    public List<Product> getProducts() {
        return listWithProducts
                .stream()
                .map(ListWithProduct::getProduct)
                .collect(Collectors.toList());
    }

//    public int getSize() {
//        return tasks.size();
//    }

//    public void deleteTask(Task task) {
//        int index = tasks.indexOf(task);
//        if (index >= 0) {
//            deleteTask(index);
//        }
//    }

    //    private void deleteTask(int index) {
//        if (index < 0) {
//            throw new NullPointerException("Task not found");
//        }
//
//        tasks.remove(index);
//    }
//
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(id + " " + name);
        if (getProducts().size() > 0) {
            builder.append("\nProducts:\n--------------------\n");
        }
        if (getProducts().size() > 0) {
            builder.append("--------------------");
        }
        return builder.toString();
    }


}
