package org.oldman.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.oldman.entities.*;

import java.util.List;

@Path("/items")
public class ItemsResource {
    @GET
    public List<Product> getItems() {
        return null;
    }
}
