package org.oldman.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import org.oldman.entities.ProductList;
import org.oldman.entities.entityUtils.EntityValidator;

import java.util.List;

@ApplicationScoped
public class ProductListRepository implements PanacheRepository<ProductList> {
    public List<ProductList> findAllFetchProduct() {
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

    public ProductList findByIdFetchProduct(Long id) {
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

    public void checkExistsById(Long id) {
        long count = count("id = :id", Parameters.with("id", id));

//        System.out.println(count);
        if (count == 0L) {
            throw new NotFoundException("There is no list with id " + id);
        }
    }

//    public ProductList findByIdFetchProductDto(Long id) {
//        final ProductList productList = find
//    }
}
