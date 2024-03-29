package org.oldman.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.instancio.Instancio;
import org.oldman.entities.ProductList;
import org.oldman.entities.enums.Priority;
import org.oldman.entities.enums.ProductCategory;
import org.oldman.services.ProductListService;

import java.net.URI;

import static org.instancio.Select.field;
import static org.oldman.entities.entityUtils.ServiceOperationUtils.applyFunction;
import static org.oldman.entities.entityUtils.ServiceOperationUtils.consumeOperation;

@Path("/product-lists")
public class ProductListResource implements BaseItemListResource<ProductList> {
    @Inject
    ProductListService service;

    @GET
    @Override
    public Response getAll() {
        return applyFunction(service, ProductListService::findAll);
    }

    @GET
    @Path("/{id}")
    @Override
    public Response getById(@PathParam("id") Long id) {
        return applyFunction(service, s -> s.findByIdFetchProduct(id));
    }

    @POST
    @Transactional
    @Override
    public Response create(@QueryParam("g") Boolean generate, ProductList list) {
        if (generate != null && generate) {
            list = Instancio.of(ProductList.class)
                    .ignore(field(ProductList.class, "id"))
                    .create();
        }
        service.saveProductList(list);

        return Response.created(URI.create("/task-lists/" + list.getId())).build();
//        return consumeOperation(service, s -> s.saveProductList(list));
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Override
    public Response update(@PathParam("id") Long id, @QueryParam("g") Boolean generate, ProductList productList) {
        return consumeOperation(service, s -> s.updateProductList(id, productList));
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Override
    public Response delete(@PathParam("id") Long id) {
        return consumeOperation(service, s -> s.deleteProductList(id));
    }

    @PUT
    @Path("/{id}/add-product/{itemId}")
    @Transactional
    @Override
    public Response addItem(@PathParam("id") Long listId, @PathParam("itemId") Long itemId) {
        return consumeOperation(service, s -> s.addProduct(listId, itemId));
    }

    // TODO: add redirect
    @PUT
    @Path("/{id}/delete-product/{listWithProductId}")
    @Transactional
    @Override
    public Response deleteItem(@PathParam("id") Long listId, @PathParam("listWithProductId") Long listWithProductId) {
        return consumeOperation(
                service,
                s -> s.deleteProductFromList(listId, listWithProductId),
                Response.Status.NO_CONTENT
        );
    }

    @PATCH
    @Path("/{id}/clear")
    @Override
    public Response clearList(@PathParam("id") Long listId) {
        return consumeOperation(service, s -> s.clearList(listId));
    }

    @PATCH
    @Path("/{listId}/products/{listWithProductId}/change")
    public Response changePriority(
            @PathParam("listId") Long listId,
            @PathParam("listWithProductId") Long listWithProductId,
            @QueryParam("priority") Priority priority,
            @QueryParam("category") ProductCategory category) {
        return consumeOperation(service, s -> s.updateProductData(listId, listWithProductId, priority, category));
    }

    @PATCH
    @Path("/listId/products/{listWithProductId}/done")
    public Response changeStatus(
            @PathParam("listId") Long listId,
            @PathParam("listWithProductId") Long listWithProductId
    ) {
        return consumeOperation(service, s -> s.changeProductStatus(listId, listWithProductId));
    }

    @PUT
    @Path("/{listId}/products/{listWithProductId}/move/{newListId}")
    public Response moveToOtherList(
            @PathParam("listId") Long listId,
            @PathParam("listWithProductId") Long listWithProductId,
            @PathParam("newListId") Long newListId
    ) {
        return consumeOperation(service, s -> s.moveToOtherList(listId, listWithProductId, newListId));
    }
}
