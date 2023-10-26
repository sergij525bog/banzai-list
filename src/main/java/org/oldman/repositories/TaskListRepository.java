package org.oldman.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import org.oldman.entities.TaskList;
import org.oldman.entities.entityUtils.EntityValidator;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TaskListRepository implements PanacheRepository<TaskList> {
    public List<TaskList> findAllLists() {
        return list("select l from TaskList l");
    }

    public List<TaskList> findAllTaskListsFetchTask() {
        return list("select distinct l from TaskList l " +
                "left join fetch l.listWithTasks lt " +
                "left join fetch lt.task t");
    }

    public TaskList findItemListByIdFetchTask(long id) {
        final TaskList list = find("select l from TaskList l " +
                        "left join fetch l.listWithTasks lt " +
                        "left join fetch lt.task " +
                        "where l.id = :listId",
                Parameters.with("listId", id)
        ).firstResult();
        return EntityValidator.returnOrThrowIfNull(list, "There is no list with id " + id);
    }

    public TaskList findByIdFetchTask(long listId) {
        final TaskList list = find("select distinct l from TaskList l " +
                        "left join fetch l.listWithTasks lt " +
                        "left join fetch lt.task t " +
                        "where l.id = :listId ",
                Parameters.with("listId", listId)
        ).firstResult();
        return EntityValidator.returnOrThrowIfNull(list, "There is no list with id " + listId);
    }

    public TaskList findItemListByIdWithoutAnyJoin(long listId) {
        final TaskList list = find("select l from TaskList l " +
                        "where l.id = :listId ",
                Parameters.with("listId", listId)
        ).firstResult();
        return EntityValidator.returnOrThrowIfNull(list, "There is no list with id " + listId);
    }
}
