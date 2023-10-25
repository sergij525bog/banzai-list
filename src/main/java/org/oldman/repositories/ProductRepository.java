package org.oldman.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import org.oldman.entities.Product;
import org.oldman.entities.entityUtils.EntityValidator;

import java.util.List;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
    public List<Product> findAllProducts() {
        return list("select p from Product p");
    }

    public Product findProductById(Long id) {
        final Product product = find("select p from Product p " +
                        "where p.id = :id",
                Parameters.with("id", id))
                .firstResult();
        return EntityValidator.returnOrThrowIfNull(product, "There is no product with id" + id);
    }
}
