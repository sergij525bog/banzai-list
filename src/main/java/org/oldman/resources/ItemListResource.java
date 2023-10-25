package org.oldman.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.instancio.Instancio;
import org.oldman.entities.ItemList;
import org.oldman.entities.enums.Priority;
import org.oldman.entities.enums.TaskCategory;
import org.oldman.services.ItemListService;

import java.net.URI;

import static org.instancio.Select.field;
import static org.oldman.entities.entityUtils.ServiceOperationUtils.applyFunction;
import static org.oldman.entities.entityUtils.ServiceOperationUtils.consumeOperation;

@Path("task-lists")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemListResource {

    @Inject
    ItemListService service;

    @GET
    public Response getAllTaskLists() {
        return applyFunction(service, ItemListService::findAllItemListsWithTasksAndProducts);
    }

    @GET
    @Path("/{id}")
    public Response getList(@PathParam("id") Long id) {
        return applyFunction(service, s -> s.findItemListJoinFetchTaskAndProductById(id));
    }

    @POST
    @Transactional
    public Response createList(ItemList list, @QueryParam("g") Boolean generate) {
        if (generate != null && generate) {
            list = Instancio.of(ItemList.class)
                    .ignore(field(ItemList.class, "id"))
                    .create();
        }

        service.saveItemList(list);

        return Response.created(URI.create("/task-lists/" + list.getId())).build();
    }

    // TODO: add redirect
    @PUT
    @Path("/{id}/add-task/{taskId}")
    @Transactional
    public Response addTask(@PathParam("id") long listId, @PathParam("taskId") long taskId) {
        return consumeOperation(service, s -> s.addTaskToList(listId, taskId));
    }

    // TODO: add redirect
    @PUT
    @Path("/{id}/delete-task/{taskId}")
    @Transactional
    public Response deleteTask(@PathParam("id") long listId, @PathParam("taskId") long taskId) {
        return consumeOperation(
                service,
                s -> s.deleteTaskFromList(listId, taskId),
                Response.Status.NO_CONTENT
        );
    }

    @PUT
    @Path("/{id}/rename")
    @Transactional
    public Response renameList(@PathParam("id") long id, @QueryParam("g") Boolean generate, String name) {
        if (generate != null && generate) {
            name = Instancio.of(String.class).create();
        }

        ItemList list = service.findItemListJoinFetchTaskAndProductById(id);
        list.setName(name);
        return Response.ok().build();
    }

    @GET
    @Path("/list-count")
    public int getListCount() {
        return service.findAllTItemListsJoinFetchTaskAndProductCount();
    }

    @GET
    @Path("/{id}/task-count")
    public int getTaskCount(@PathParam("id") long id) {
        return service.getTaskCount(id);
    }

    @GET
    @Path("/{id}/tasks")
    public Response getTasksOfList(@PathParam("id") long id) {
        return applyFunction(service, s -> s.findItemListsJoinFetchTask(id).getTasks());
    }

    @PATCH
    @Path("/{id}/clear")
    public Response clearList(@PathParam("id") long listId) {
        return consumeOperation(service, s -> s.clearList(listId));
    }

    @PATCH
    @Path("/{listId}/{taskId}/priority")
    public Response changePriority(
            @PathParam("listId") long listId,
            @PathParam("taskId") long taskId,
            Priority priority) {
        return consumeOperation(service, s -> s.changeTaskPriority(listId, taskId, priority));
    }

    @PATCH
    @Path("/{listId}/{taskId}/category")
    public Response changeCategory(
            @PathParam("listId") long listId,
            @PathParam("taskId") long taskId,
            TaskCategory category
    ) {
        return consumeOperation(service, s -> s.changeTaskCategory(listId, taskId, category));
    }
}
