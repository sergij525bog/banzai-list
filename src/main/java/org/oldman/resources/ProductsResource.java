package org.oldman.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.instancio.Instancio;
import org.oldman.entities.Product;
import org.oldman.entities.ProductList;
import org.oldman.services.ProductService;

import java.net.URI;

import static org.instancio.Select.field;
import static org.oldman.entities.entityUtils.ServiceOperationUtils.applyFunction;
import static org.oldman.entities.entityUtils.ServiceOperationUtils.consumeOperation;

@Path("/products")
public class ProductsResource {
    @Inject
    ProductService service;

    @GET
    public Response getAll() {
        return applyFunction(service, ProductService::findAll);
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return applyFunction(service, s -> s.findById(id));
    }

    @POST
    @Transactional
    public Response addProduct(Product product, @QueryParam("g") Boolean generate) {
        if (generate != null && generate) {
            product = Instancio.of(Product.class)
                    .ignore(field(Product.class, "id"))
                    .create();
        }
        service.saveProduct(product);

        return Response.created(URI.create("/task-lists/" + product.getId())).build();
//        return consumeOperation(service, s -> s.saveProduct(product));
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, @QueryParam("g") Boolean generate, Product product) {
        if (generate != null && generate) {
            product = Instancio
                    .of(Product.class)
                    .ignore(field("id"))
                    .create();
        }
//      return consumeOperation(service, s -> s.updateTask(id, task));
        try {
            service.updateProduct(id, product);
        } catch (NotFoundException e) {
            return Response
                    .status(
                            Response.Status.NOT_FOUND.getStatusCode(),
                            e.getMessage()
                    )
                    .build();
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        return consumeOperation(service, s -> s.deleteById(id));
    }


}
