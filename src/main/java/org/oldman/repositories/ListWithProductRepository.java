package org.oldman.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import org.oldman.entities.ListWithProduct;
import org.oldman.entities.entityUtils.EntityValidator;

import java.util.Map;

@ApplicationScoped
public class ListWithProductRepository implements PanacheRepository<ListWithProduct> {
    public void deleteAllByProductList(Long productListId) {
        delete("delete from ListWithProduct lp " +
                "where lp.productList.id = :productListId",
                Parameters.with("productListId", productListId));
    }

    public void deleteAllByProduct(Long productId) {
        delete("delete from ListWithProduct lp " +
                "where lp.product = :productId",
                Parameters.with("productId", productId));
    }

    public ListWithProduct findByIdAndList(Long listWithProductId, Long listId) {
        final ListWithProduct listWithProduct = find(
                "select lp from ListWithProduct lp " +
                        "where lp.id = :listWithProductId " +
                        "and lp.productList.id = :listId",
                Parameters
                        .with("listWithProductId", listWithProductId)
                        .and("listId", listId)
        ).firstResult();
        return EntityValidator.returnOrThrowIfNull(
                listWithProduct,
                "Product list doe not contain list with product with id " + listWithProductId
        );
    }

    public void checkContainedInList(Long listId, Long listWithProductId) {
        long count = count(
                "id = :listWithTaskId and taskList.id = :listId",
                Parameters.with("listWithTaskId", listWithProductId)
                        .and("listId", listId)
        );

//        System.out.println(count);
        if (count == 0L) {
            throw new NotFoundException("List does not contain list with task with id " + listWithProductId);
        }
    }

    public void updateById(Long id, Map<String, Object> updateParams) {
        String query = "update ListWithProduct lp set";
        String where = "where lp.id = :id";
        Parameters parameters = Parameters.with("id", id);
        String tableAlias = "lp";

        try {
            QueryEditor editor = QueryEditor.getEditor(query, parameters, tableAlias, updateParams);
            update(editor.getQuery() + where, editor.getParameters());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void moveToOtherList(Long id, Long newListId) {
        update(
                "update ListWithProduct lp set " +
                "lp.productList.id = :newListId " +
                "where lp.id = :id",
                Parameters
                        .with("id", id)
                        .and("newListId", newListId));
    }

    public void checkExistsById(Long id) {
        long count = count("id = :id", Parameters.with("id", id));

//        System.out.println(count);
        if (count == 0L) {
            throw new NotFoundException("There is no list with id " + id);
        }
    }
}
