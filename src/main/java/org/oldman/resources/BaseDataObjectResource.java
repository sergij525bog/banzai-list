package org.oldman.resources;

import jakarta.ws.rs.core.Response;
import org.oldman.entities.DataObject;

public interface BaseDataObjectResource<T extends DataObject> {
    Response getAll();
    Response getById(Long id);
    Response create(Boolean generate, T data);
    Response update(Long id, Boolean generate, T data);
    Response delete(Long id);
}
