package org.oldman.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.instancio.Instancio;
import org.oldman.entities.ProductList;
import org.oldman.entities.enums.Priority;
import org.oldman.services.ProductListService;

import java.net.URI;

import static org.instancio.Select.field;
import static org.oldman.entities.entityUtils.ServiceOperationUtils.applyFunction;
import static org.oldman.entities.entityUtils.ServiceOperationUtils.consumeOperation;

@Path("/product-lists")
public class ProductListResource {
    @Inject
    ProductListService service;

    @GET
    public Response getAll() {
        return applyFunction(service, ProductListService::findAll);
    }

    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") Long id) {
        return applyFunction(service, s -> s.findProductListByIdJoinFetchProduct(id));
    }

    @POST
    @Transactional
    public Response createProductList(ProductList list, @QueryParam("g") Boolean generate) {
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
    public Response updateProductList(@PathParam("id") Long id, ProductList productList) {
        return consumeOperation(service, s -> s.updateProductList(id, productList));
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteProductList(@PathParam("id") Long id) {
        return consumeOperation(service, s -> s.deleteProductList(id));
    }

    @PUT
    @Path("/{id}/add-product/{productId}")
    @Transactional
    public Response addProduct(@PathParam("id") long listId, @PathParam("productId") long productId) {
        return consumeOperation(service, s -> s.addProduct(listId, productId));
    }

    // TODO: add redirect
    @PUT
    @Path("/{id}/delete-product/{productId}")
    @Transactional
    public Response deleteProduct(@PathParam("id") long listId, @PathParam("productId") long productId) {
        return consumeOperation(
                service,
                s -> s.deleteProductFromList(listId, productId),
                Response.Status.NO_CONTENT
        );
    }

    @PATCH
    @Path("/{id}/clear")
    public Response clearList(@PathParam("id") long listId) {
        return consumeOperation(service, s -> s.clearList(listId));
    }

    @PATCH
    @Path("/{listId}/{productId}/priority")
    public Response changePriority(
            @PathParam("listId") long listId,
            @PathParam("productId") long productId,
            Priority priority) {
        return consumeOperation(service, s -> s.changeProductPriority(listId, productId, priority));
    }
}
