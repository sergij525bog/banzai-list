package org.oldman.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.instancio.Instancio;
import org.oldman.entities.Task;
import org.oldman.entities.enums.Priority;
import org.oldman.entities.enums.TaskCategory;
import org.oldman.services.TaskService;

import java.net.URI;

import static org.instancio.Select.field;
import static org.oldman.entities.entityUtils.ServiceOperationUtils.applyFunction;
import static org.oldman.entities.entityUtils.ServiceOperationUtils.consumeOperation;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource implements BaseItemResource<Task> {
    @Inject
    TaskService service;

    @GET
    @Override
    public Response getAll() {
        return applyFunction(service, TaskService::findAll);
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
    public Response create(@QueryParam("g") Boolean generate, Task task) {
        if (generate != null && generate) {
            task = Instancio
                    .of(Task.class)
                    .ignore(field("id"))
                    .create();
        }

        /*consumeOperation(service, s -> s.saveTask(task), Response
                .created(URI.create("/tasks/" + task.getId()))
                .build());*/
        service.save(task);
        return Response
                .created(URI.create("/tasks/" + task.getId()))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Override
    public Response update(@PathParam("id") Long id, @QueryParam("g") Boolean generate, Task task) {
        if (generate != null && generate) {
            task = Instancio
                    .of(Task.class)
                    .ignore(field("id"))
                    .create();
        }
//      return consumeOperation(service, s -> s.updateTask(id, task));
        try {
            service.update(id, task);
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

//    private <T> void updateNonNullableField(T attribute, Consumer<T> setter) {
//        // Examples of using
//        /*updateNonNullableField(task.getName(), entity::setName);
//        updateNonNullableField(task.getPeriod(), entity::setPeriod);*/
//        if (attribute != null) {
//            setter.accept(attribute);
//        }
//    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Override
    public Response delete(@PathParam("id") Long id) {
        return consumeOperation(service, s -> s.delete(id), Response.Status.NO_CONTENT);
    }

    @GET
    @Path("/by-list/{id}")
    public Response getTasksByList(
            @PathParam("id") Long listId,
            @QueryParam("category") TaskCategory category,
            @QueryParam("priority") Priority priority) {
        return applyFunction(service, s -> s.findAllByList(listId, category, priority));
    }

    @GET
    @Path("/search/{name}")
    public Response searchByName(String name) {
        // show page with message
        return applyFunction(service, s -> s.findByName(name));
    }

    @GET
    @Path("/count")
    public Response count() {
        return applyFunction(service, TaskService::count);
    }
}
