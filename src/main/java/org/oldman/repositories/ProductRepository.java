package org.oldman.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import org.oldman.entities.Product;
import org.oldman.entities.entityUtils.EntityValidator;
import org.oldman.entities.enums.ProductCategory;

import java.util.List;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
    public List<Product> findAllProducts() {
        return list("select p from Product p");
    }

    public Product findProductById(Long id) {
        final Product product = find(
                "select p from Product p " +
                        "where p.id = :id",
                Parameters.with("id", id)
        ).firstResult();
        return EntityValidator.returnOrThrowIfNull(product, "There is no product with id" + id);
    }

    public void checkExistsById(Long id) {
        long count = count("id = :id", Parameters.with("id", id));

//        System.out.println(count);
        if (count == 0L) {
            throw new NotFoundException("There is no list with id " + id);
        }
    }

    public void updateByListWithProduct(Long listWithProductId, ProductCategory category) {
        update(
                "update Product p set " +
                        "category = :category " +
                        "where p.listWithProduct.id = :listWithProductId",
                Parameters
                        .with("listWithProductId", listWithProductId)
                        .and("category", category)
        );
    }
}
