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
import org.oldman.models.TaskDto;
import org.oldman.repositories.bulders.*;
import org.oldman.repositories.bulders.join.JoinData;
import org.oldman.repositories.bulders.join.JoinDirector;
import org.oldman.services.TaskService;

import java.net.URI;
import java.util.List;

import static org.instancio.Select.field;
import static org.oldman.entities.entityUtils.ServiceOperationUtils.applyFunction;
import static org.oldman.entities.entityUtils.ServiceOperationUtils.consumeOperation;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {
    @Inject
    TaskService service;

    @GET
//    @Override
    public Response getAll() {
//        service.checkExists();
//        System.out.println("Get all");
        QueryBuilder builder = new QueryBuilder();
        TableInfo tableInfo = new TableInfo("Task", "t");

        String query = builder.from(tableInfo)
                .join(FieldInfo.createWithAlias("t.listWithTasks", "lwt"))
                .joinOnFetch(new TableInfo("TaskList", "tl")).on(FieldInfo.createWithoutAlias("t.id"), FieldInfo.createWithoutAlias("tl.name"))
                .leftJoinOnFetch(tableInfo).on(FieldInfo.createWithoutAlias("tl.name"), FieldInfo.createWithoutAlias("tl.name"))
                .joinOn(tableInfo).on(FieldInfo.createWithoutAlias("lwt.task.id"), FieldInfo.createWithoutAlias("t.id"))
                .where(FieldInfo.createWithoutAlias("t.name"), Operator.EQUAL, ":name")
                .and(FieldInfo.createWithoutAlias( "t.id"), Operator.LESS_OR_EQUAL, ":id")
                .build();
        System.out.println(query);
//        return applyFunction(service, TaskService::findAll);
        return null;
    }

    @GET
    @Path("/{id}")
//    @Override
    public Response getById(@PathParam("id") Long id) {
        return applyFunction(service, s -> s.findDtoById(id));
    }

    @POST
    @Transactional
//    @Override
    public Response create(@QueryParam("g") Boolean generate, TaskDto task) {
        if (generate != null && generate) {
            task = Instancio
                    .of(TaskDto.class)
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
//    @Override
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
//    @Override
    public Response delete(@PathParam("id") Long id) {
        return consumeOperation(service, s -> s.delete(id), Response.Status.NO_CONTENT);
    }

    @GET
    @Path("/by-list/{id}")
    public Response getTasksByList(
            @PathParam("id") Long listId,
            @QueryParam("category") TaskCategory category,
            @QueryParam("priority") Priority priority,
//            @QueryParam("done") Boolean done,
            @QueryParam("sortBy") List<String> sortBy,
            @QueryParam("sortOrder") String sortOrder
    ) {
//        System.out.println(sortOrder.toString());
//        sortBy.forEach(System.out::println);
        return applyFunction(
                service,
                s -> s.findAllByList(
                        listId,
                        category,
                        priority,
                        null,
                        sortBy,
                        sortOrder
                ));
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
