package org.oldman.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.instancio.Instancio;
import org.oldman.entities.TaskList;
import org.oldman.entities.enums.Priority;
import org.oldman.entities.enums.TaskCategory;
import org.oldman.services.TaskListService;

import java.net.URI;

import static org.instancio.Select.field;
import static org.oldman.entities.entityUtils.ServiceOperationUtils.applyFunction;
import static org.oldman.entities.entityUtils.ServiceOperationUtils.consumeOperation;

@Path("task-lists")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskListResource implements BaseItemListResource<TaskList> {
    @Inject
    TaskListService service;

    @GET
    @Override
    public Response getAll() {
        return applyFunction(service, TaskListService::findAllTaskListsFetchTask);
//        return super.getAll();
    }

    @GET
    @Path("/{id}")
    @Override
    public Response getById(@PathParam("id") Long id) {
        return applyFunction(service, s -> s.findByIdFetchTask(id));
//        return super.getById(id);
    }

    @POST
    @Transactional
    @Override
    public Response create(@QueryParam("g") Boolean generate, TaskList list) {
        if (generate != null && generate) {
            list = Instancio.of(TaskList.class)
                    .ignore(field(TaskList.class, "id"))
                    .create();
        }

        service.saveItemList(list);

        return Response.created(URI.create("/task-lists/" + list.getId())).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Override
    public Response update(@PathParam("id") Long id, @QueryParam("g") Boolean generate, TaskList taskList) {
        return consumeOperation(service, s -> s.update(id, taskList));
//        return super.update(id, generate, taskList);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Override
    public Response delete(@PathParam("id") Long id) {
        return consumeOperation(service, s -> s.deleteList(id));
//        return super.delete(id);
    }

    // TODO: add redirect
    @PUT
    @Path("/{id}/add-task/{itemId}")
    @Transactional
    @Override
    public Response addItem(@PathParam("id") Long listId, @PathParam("itemId") Long itemId) {
        return consumeOperation(service, s -> s.addTaskToList(listId, itemId));
    }

    // TODO: add redirect
    @PUT
    @Path("/{id}/delete-task/{itemId}")
    @Transactional
    @Override
    public Response deleteItem(@PathParam("id") Long listId, @PathParam("itemId") Long itemId) {
        return consumeOperation(
                service,
                s -> s.deleteTaskFromList(listId, itemId),
                Response.Status.NO_CONTENT
        );
    }

    @PUT
    @Path("/{id}/rename")
    @Transactional
    public Response renameList(@PathParam("id") Long id, @QueryParam("g") Boolean generate, String name) {
        if (generate != null && generate) {
            name = Instancio.of(String.class).create();
        }

        TaskList list = service.findByIdFetchTask(id);
        list.setName(name);
        return Response.ok().build();
    }

    @GET
    @Path("/list-count")
    public int getListCount() {
        return service.findItemListsCount();
    }

    @GET
    @Path("/{id}/task-count")
    public int getTaskCount(@PathParam("id") Long id) {
        return service.getTaskCount(id);
    }

    @GET
    @Path("/{id}/tasks")
    public Response getTasksOfList(@PathParam("id") Long id) {
        return applyFunction(service, s -> s.findByIdFetchTask(id).getTasks());
    }

    @PATCH
    @Path("/{id}/clear")
    @Override
    public Response clearList(@PathParam("id") Long listId) {
        return consumeOperation(service, s -> s.clearList(listId));
    }

    @PATCH
    @Path("/{listId}/{taskId}/priority")
    public Response changePriority(
            @PathParam("listId") Long listId,
            @PathParam("taskId") Long taskId,
            Priority priority) {
        return consumeOperation(service, s -> s.changeTaskPriority(listId, taskId, priority));
    }

    @PATCH
    @Path("/{listId}/{taskId}/category")
    public Response changeCategory(
            @PathParam("listId") Long listId,
            @PathParam("taskId") Long taskId,
            TaskCategory category
    ) {
        return consumeOperation(service, s -> s.changeTaskCategory(listId, taskId, category));
    }
}
