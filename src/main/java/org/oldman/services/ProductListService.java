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

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProductListService {
    @Inject
    ProductListRepository productListRepository;

    @Inject
    ListWithProductRepository joinTableRepository;

    @Inject
    ProductRepository productRepository;

    public List<ProductList> findAll() {
        return productListRepository.findAllProductListsJoinFetchProduct();
    }

    public ProductList findProductListById(Long id) {
        return productListRepository.findProductListById(id);
    }

    public ProductList findProductListByIdJoinFetchProduct(Long id) {
        return productListRepository.findProductListByIdJoinFetchProduct(id);
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
        final ProductList productList = findProductListByIdJoinFetchProduct(id);
        joinTableRepository.deleteAllByProductList(id);
        productListRepository.delete(productList);
    }

    @Transactional
    public void addProduct(long listId, long productId) {
        final ProductList productList = productListRepository.findProductListByIdJoinFetchProduct(listId);
        final Product product = productRepository.findProductById(productId);

        final boolean listContainsProduct = productList
                .getListWithProducts()
                .stream()
                .anyMatch(lp -> lp.getProduct().equals(product));

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
    public void deleteProductFromList(long listId, long productId) {
        joinTableRepository.delete(getListWithProduct(listId, productId));
    }

    @Transactional
    public void clearList(long listId) {
//        TODO: this line is only for validation list is in db. It should be replaced with less expensive operation
        final ProductList list = findProductListById(listId);

        joinTableRepository.deleteAllByProductList(listId);
    }

    @Transactional
    public void changeProductPriority(long listId, long productId, Priority priority) {
        if (priority == null) {
            throw new IllegalArgumentException("You don't pass a new priority");
        }

        final ListWithProduct listWithProduct = getListWithProduct(listId, productId);
        listWithProduct.setPriority(priority);
    }

    private ListWithProduct getListWithProduct(long listId, long productId) {
        final ProductList productList = productListRepository.findProductListByIdJoinFetchProduct(listId);
        final Product product = productRepository.findProductById(productId);

        final Optional<ListWithProduct> listWithProduct = productList
                .getListWithProducts()
                .stream()
                .filter(lp -> lp.getProduct().equals(product))
                .findFirst();

        if (listWithProduct.isEmpty()) {
            throw new IllegalArgumentException("List does not contain product with id " + productId);
        }
        return listWithProduct.get();
    }

    public void changeProductCategory(Long listId, Long listWithProductId, ProductCategory category) {
        if (category == null) {
            throw new IllegalArgumentException("You don't pass a new category");
        }

        final ListWithProduct listWithProduct = getListWithProduct(listId, listWithProductId);
        listWithProduct.getProduct().setProductCategory(category);
    }

    public void changeProductStatus(Long listId, Long listWithProductId) {
        final ListWithProduct listWithTask = getListWithProduct(listId, listWithProductId);
        listWithTask.setDone(!listWithTask.isDone());
    }

    @Transactional
    public void moveToOtherList(Long listId, Long listWithProductId, Long newListId) {
        final ListWithProduct listWithProduct = getListWithProduct(listId, listWithProductId);
        final ProductList newList = productListRepository.findProductListByIdJoinFetchProduct(newListId);
        listWithProduct.setProductList(newList);
        newList.getListWithProducts().add(listWithProduct);
    }
}
