package org.oldman.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import org.oldman.entities.Task;
import org.oldman.entities.entityUtils.EntityValidator;
import org.oldman.entities.enums.Priority;
import org.oldman.entities.enums.TaskCategory;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TaskRepository implements PanacheRepository<Task> {
    public List<Task> findAllTasks() {
        return list("select t from Task t");
    }

    public Task findTaskById(long id) {
        final Task task = find("select t from Task t " +
                        "where t.id = :taskId",
                Parameters.with("taskId", id))
                .firstResult();
        return EntityValidator.returnOrThrowIfNull(task, "There is no task with id " + id);
    }

    public Optional<Task> findTaskByIdOptional(long id) {
        return find("select t from Task t " +
                "where t.id = :taskId",
                Parameters.with("taskId", id))
                .firstResultOptional();
    }

    public Optional<Task> findTaskAndListWithTaskByIdOptional(long id) {
        return find("select t from Task t " +
                        "left join t.listWithTasks lt " +
                        "where t.id = :taskId",
                Parameters.with("taskId", id))
                .firstResultOptional();
    }

    public Task findTaskByName(String name) {
        final Task task = find("select t from Task t " +
                        "where t.name = :name",
                Parameters.with("name", name))
                .firstResult();
        return EntityValidator.returnOrThrowIfNull(task, "There is no task with name " + name);
    }

    public List<Task> getAllTasksByListId(long listId) {
        return list("select t from Task t " +
                "join t.listWithTasks lt " +
                "join lt.taskList l " +
                "where l.id = :listId",
                Parameters.with("listId", listId));
    }

    public List<Task> getAllTasksByListIdAndPriority(long listId, Priority priority) {
        return list("select t from Task t " +
                        "join fetch t.listWithTasks lt " +
                        "join lt.taskList l " +
                        "where l.id = :listId and " +
                        "lt.priority = :priority",
                Parameters.with("listId", listId)
                        .and("priority", priority));
    }

    public List<Task> getAllTasksByListIdAndCategory(long listId, TaskCategory category) {
        return list("select t from Task t " +
                        "join fetch t.listWithTasks lt " +
                        "join lt.taskList l " +
                        "where l.id = :listId and " +
                        "lt.taskCategory = :category",
                Parameters.with("listId", listId)
                        .and("category", category)
        );
    }

    public List<Task> getAllTasksByListIdAndCategoryAndPriority(long listId, TaskCategory category, Priority priority) {
        return list("select t from Task t " +
                        "join fetch t.listWithTasks lt " +
                        "join lt.taskList l " +
                        "where l.id = :listId and " +
                        "lt.taskCategory = :category and " +
                        "lt.priority = :priority",
                Parameters.with("listId", listId)
                        .and("priority", priority)
                        .and("category", category)
        );
    }

    public Task findByIdJoinFetchItemList(long id) {
        final Task task = find("select t from Task t " +
                        "left join fetch t.listWithTasks lt " +
                        "left join fetch lt.taskList l " +
                        "where t.id = :taskId",
                Parameters.with("taskId", id))
                .firstResult();
        return EntityValidator.returnOrThrowIfNull(task, "There is no task with id " + id);
    }
}
