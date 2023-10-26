package org.oldman.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.instancio.Instancio;
import org.oldman.entities.Product;
import org.oldman.services.ProductService;

import java.net.URI;

import static org.instancio.Select.field;
import static org.oldman.entities.entityUtils.ServiceOperationUtils.applyFunction;
import static org.oldman.entities.entityUtils.ServiceOperationUtils.consumeOperation;

@Path("/products")
public class ProductsResource implements BaseItemResource<Product> {
    @Inject
    ProductService service;

    @GET
    @Override
    public Response getAll() {
        return applyFunction(service, ProductService::findAll);
    }

    @GET
    @Path("/{id}")
    @Override
    public Response getById(@PathParam("id") Long id) {
        return applyFunction(service, s -> s.findById(id));
    }

    @POST
    @Transactional
    @Override
    public Response create(@QueryParam("g") Boolean generate, Product product) {
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
    @Override
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
    @Override
    public Response delete(@PathParam("id") Long id) {
        return consumeOperation(service, s -> s.deleteById(id));
    }


}
