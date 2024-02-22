package org.oldman.models;

import org.oldman.entities.enums.Priority;
import org.oldman.entities.enums.ProductCategory;

import java.time.LocalDateTime;

public class ProductDto implements Dto {
    private Long id;
    private String name;
    private Long productId;
    private int count;
    private LocalDateTime endDate = null;
    private Priority priority = Priority.LOW;
    private ProductCategory productCategory = ProductCategory.NONE;
    private boolean retrieved = false;
    private double weight;
    private boolean done = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public boolean isRetrieved() {
        return retrieved;
    }

    public void setRetrieved(boolean retrieved) {
        this.retrieved = retrieved;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }
}
