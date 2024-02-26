package org.oldman.repositories;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import org.oldman.entities.Task;
import org.oldman.entities.entityUtils.EntityValidator;
import org.oldman.models.TaskDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@ApplicationScoped
public class TaskRepository implements PanacheRepository<Task> {
    public List<Task> projectTest() {
        return null;
    }
    public List<Task> findAllTasks() {
        return list("select t from Task t");
    }

    public Task findById(Long id) {
        final Task task = find("select t from Task t " +
                        "where t.id = :taskId",
                Parameters.with("taskId", id))
                .firstResult();
        return EntityValidator.returnOrThrowIfNull(task, "There is no task with id " + id);
    }

    public TaskDto findDtoById(Long id) {
        final TaskDto task = find("select t from Task t " +
                        "where t.id = :taskId",
                Parameters.with("taskId", id))
                .project(TaskDto.class)
                .firstResult();
        return EntityValidator.returnOrThrowIfNull(task, "There is no task with id " + id);
    }

    public Task findTaskByName(String name) {
        final Task task = find("select t from Task t " +
                        "where t.name = :name",
                Parameters.with("name", name))
                .firstResult();
        return EntityValidator.returnOrThrowIfNull(task, "There is no task with name " + name);
    }

    public List<Task> getAllTasksByListId(Long listId) {
        return list("select t from Task t " +
                "join t.listWithTasks lt " +
                "where lt.taskList.id = :listId",
                Parameters.with("listId", listId));
    }

    public Task findByIdJoinFetchTaskList(Long id) {
        final Task task = find("select t from Task t " +
                        "left join fetch t.listWithTasks lt " +
                        "left join fetch lt.taskList l " +
                        "where t.id = :taskId",
                Parameters.with("taskId", id))
                .firstResult();
        System.out.println(task);
        return EntityValidator.returnOrThrowIfNull(task, "There is no task with id " + id);
    }

    public void checkExistsById(Long id) {
        PanacheQuery<Integer> query = find("select count(*) from (select t from Task t where id = :id limit 1)",
                Parameters.with("id", id))
                .project(Integer.class);
        System.out.println("Result: " + id);
    }

    public void checkExistsByIdWithFullSelect(Long id) {
        count("t from Task t " +
                        "where t.id = :id",
                Parameters.with("id", id));
    }

    public Stream<Task> getAllByListWithFiltersAsStream(Long listId, Map<String, Object> filters, List<String> sortBy, String sortOrder) {
        String baseQuery = "select t from Task t " +
                "join fetch t.listWithTasks lt " +
                "where lt.taskList.id = :listId";
        Parameters parameters = Parameters.with("listId", listId);
        final String tableAlias = "lt";

        QueryEditor editor = QueryEditor.getEditor(baseQuery, parameters, tableAlias, filters, sortBy, sortOrder);

        return editor != null ?
                stream(editor):
                stream(baseQuery, parameters);
    }

    private Stream<Task> stream(QueryEditor editor) {
        return stream(editor.getQuery(), editor.getParameters());
    }
}
