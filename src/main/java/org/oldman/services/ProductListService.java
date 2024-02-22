package org.oldman.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.oldman.entities.ListWithProduct;
import org.oldman.entities.Product;
import org.oldman.entities.ProductList;
import org.oldman.entities.enums.Priority;
import org.oldman.entities.enums.ProductCategory;
import org.oldman.repositories.ListWithProductRepository;
import org.oldman.repositories.ProductListRepository;
import org.oldman.repositories.ProductRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ProductListService {
    @Inject
    ProductListRepository productListRepository;

    @Inject
    ListWithProductRepository joinTableRepository;

    @Inject
    ProductRepository productRepository;

    public List<ProductList> findAll() {
        return productListRepository.findAllFetchProduct();
    }

    public ProductList findProductListById(Long id) {
        return productListRepository.findProductListById(id);
    }

    public ProductList findByIdFetchProduct(Long id) {
        return productListRepository.findByIdFetchProduct(id);
    }

    @Transactional
    public void saveProductList(ProductList list) {
        //        EntityValidator.validateEntityBeforeSave(list);
        productListRepository.persist(list);
    }

    @Transactional
    public void updateProductList(Long id, ProductList productList) {
        final ProductList entity = findProductListById(id);
        //        EntityValidator.validateEntityBeforeSave(productList);

        entity.setName(productList.getName());
    }

    @Transactional
    public void deleteProductList(Long id) {
        productListRepository.checkExistsById(id);
        joinTableRepository.deleteAllByProductList(id);
        productListRepository.deleteById(id);
    }

    @Transactional
    public void addProduct(Long listId, Long productId) {
        final ProductList productList = productListRepository.findByIdFetchProduct(listId);
        final Product product = productRepository.findProductById(productId);

        final boolean listContainsProduct = productList
                .getListWithProducts()
                .stream()
                .anyMatch(lp -> lp.getProduct().getId().equals(productId));

        if (listContainsProduct) {
//            throw new ConflictException("List already contains this task");
            throw new IllegalArgumentException("List already contains this product");
        }
//        Task task = Instancio.of(Task.class)
//                .ignore(field(Task.class, "id"))
//                .create();

        final ListWithProduct listWithProduct = new ListWithProduct();
        listWithProduct.setProduct(product);
        listWithProduct.setProductList(productList);
        joinTableRepository.persist(listWithProduct);
    }

    @Transactional
    public void deleteProductFromList(Long listId, Long listWithProductId) {
        joinTableRepository.delete(getListWithProduct(listId, listWithProductId));
    }

    @Transactional
    public void clearList(Long listId) {
        productListRepository.checkExistsById(listId);

        joinTableRepository.deleteAllByProductList(listId);
    }

    @Transactional
    public void updateProductData(Long listId, Long listWithProductId, Priority priority, ProductCategory category) {
        if (priority == null && category == null) {
            throw new IllegalArgumentException("You don't pass new product parameters");
        }

        productListRepository.checkExistsById(listId);
        joinTableRepository.checkContainedInList(listId, listWithProductId);

        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("taskCategory", category);

        joinTableRepository.updateById(listWithProductId, updateParams);
        productRepository.updateByListWithProduct(listWithProductId, category);
    }

    public void changeProductStatus(Long listId, Long listWithProductId) {
        final ListWithProduct listWithTask = getListWithProduct(listId, listWithProductId);
        listWithTask.setDone(!listWithTask.isDone());
    }

    @Transactional
    public void moveToOtherList(Long listId, Long listWithProductId, Long newListId) {
        productListRepository.checkExistsById(listId);

        if (listId.equals(newListId)) {
            throw new IllegalArgumentException("You try to move product to the same list!");
        }

        productListRepository.checkExistsById(newListId);
        joinTableRepository.checkExistsById(listWithProductId);
        joinTableRepository.checkContainedInList(listId, listWithProductId);

        joinTableRepository.moveToOtherList(listWithProductId, newListId);
    }

    private ListWithProduct getListWithProduct(Long listId, Long listWithProductId) {
        productListRepository.checkExistsById(listId);

        return joinTableRepository.findByIdAndList(listWithProductId, listId);
    }
}
