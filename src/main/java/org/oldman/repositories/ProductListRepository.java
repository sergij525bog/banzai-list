package org.oldman.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import org.oldman.entities.ProductList;
import org.oldman.entities.entityUtils.EntityValidator;

import java.util.List;

@ApplicationScoped
public class ProductListRepository implements PanacheRepository<ProductList> {
    public List<ProductList> findAllProductListsJoinFetchProduct() {
        return list("select pl from ProductList pl " +
                "left join fetch pl.listWithProducts lp " +
                "left join fetch lp.product p");
    }

    public ProductList findProductListById(Long id) {
        final ProductList productList = find("select pl from ProductList pl " +
                        "where id = :id",
                Parameters.with("id", id))
                .firstResult();
        return EntityValidator.returnOrThrowIfNull(
                productList,
                "There is no product list with id " + id
        );
    }

    public ProductList findProductListByIdJoinFetchProduct(Long id) {
        final ProductList productList = find("select pl from ProductList pl " +
                        "left join fetch pl.listWithProducts lp " +
                        "left join fetch lp.product p " +
                        "where id = :id",
                Parameters.with("id", id))
                .firstResult();
        return EntityValidator.returnOrThrowIfNull(
                productList,
                "There is no product list with id " + id
        );
    }
}
