package org.oldman.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.oldman.entities.Product;
import org.oldman.repositories.ListWithProductRepository;
import org.oldman.repositories.ProductRepository;

import java.util.List;

@ApplicationScoped
public class ProductService {
    @Inject
    ProductRepository productRepository;

    @Inject
    ListWithProductRepository listWithProductRepository;

    public List<Product> findAll() {
        return productRepository.findAllProducts();
    }

    public Product findById(Long id) {
        return productRepository.findProductById(id);
    }

    @Transactional
    public void saveProduct(Product product) {
        //        EntityValidator.validateEntityBeforeSave(product);
        productRepository.persist(product);
    }

    @Transactional
    public void updateProduct(Long id, Product product) {
        final Product entity = findById(id);
        //        EntityValidator.validateEntityBeforeSave(product);
        entity.setName(product.getName());
        entity.setProductCategory(product.getProductCategory());
    }

    @Transactional
    public void deleteById(Long id) {
        final Product entity = findById(id);
        listWithProductRepository.deleteAllByProduct(id);
        productRepository.delete(entity);
    }
}
