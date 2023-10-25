package org.oldman.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import org.oldman.entities.ListWithProduct;

@ApplicationScoped
public class ListWithProductRepository implements PanacheRepository<ListWithProduct> {
    public void deleteAllByProductList(Long productListId) {
        delete("delete from ListWithProduct lp " +
                "where lp.productList = :productListId",
                Parameters.with("productListId", productListId));
    }

    public void deleteAllByProduct(Long productId) {
        delete("delete from ListWithProduct lp " +
                "where lp.product = :productId",
                Parameters.with("productId", productId));
    }
}
