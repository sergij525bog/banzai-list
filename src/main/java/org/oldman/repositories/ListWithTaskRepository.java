package org.oldman.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import org.oldman.entities.ListWithTask;
import org.oldman.entities.entityUtils.EntityValidator;

import java.util.List;

@ApplicationScoped
public class ListWithTaskRepository implements PanacheRepository<ListWithTask> {
    public List<ListWithTask> findAllByListFetchTask(Long listId) {
        return list("select lt from ListWithTask lt " +
                        "left join fetch lt.task " +
                        "left join lt.taskList tl " +
                        "where tl.id = :listId ",
                Parameters.with("listId", listId)
        );
    }
    
    public ListWithTask findByListAndTask(Long listId, Long taskId) {
        final ListWithTask list = find("select lt from ListWithTask lt " +
                        "where lt.taskList.id = :listId " +
                        "and lt.task.id = :taskId",
                Parameters
                        .with("listId", listId)
                        .and("taskId", taskId)
        ).firstResult();
        return EntityValidator.returnOrThrowIfNull(
                list,
                "There is no list with id " + listId + " and task with id " + taskId
        );
    }

    public ListWithTask findByListAndTaskFetchTask(Long listId, Long taskId) {
        final ListWithTask list = find("select lt from ListWithTask lt " +
                        "join fetch lt.task " +
                        "where lt.taskList.id = :listId " +
                        "and lt.task.id = :taskId",
                Parameters
                        .with("listId", listId)
                        .and("taskId", taskId)
        ).firstResult();
        return EntityValidator.returnOrThrowIfNull(
                list,
                "There is no list with id " + listId + " and task with id " + taskId
        );
    }

    public void deleteByTask(Long taskId) {
        delete("delete from ListWithTask lt " +
                "where lt.task.id = :taskId",
                Parameters.with("taskId", taskId));
    }

    public void deleteAllByItemList(Long listId) {
        delete("delete from ListWithTask lt " +
                "where lt.taskList.id = :listId",
                Parameters.with("listId", listId));
    }

    public void deleteAllByTaskList(Long taskListId) {
        delete("delete from ListWithTask lt " +
                        "where lt.taskList.id = :taskListId",
                Parameters.with("taskListId", taskListId));
    }

    public ListWithTask findByIdFetchTask(Long id) {
        final ListWithTask list = find("select lt from ListWithTask lt " +
                        "join fetch lt.task " +
                        "where lt.id = :id",
                Parameters.with("id", id)
        ).firstResult();
        return EntityValidator.returnOrThrowIfNull(list, "There is no list with task with id " + id);
    }

    public ListWithTask findByIdAndListFetchTask(Long listId, Long taskId) {
        final ListWithTask list = find("select lt from ListWithTask lt " +
                        "join fetch lt.task " +
                        "where lt.taskList.id = :listId " +
                        "and lt.id = :taskId",
                Parameters
                        .with("listId", listId)
                        .and("taskId", taskId)
        ).firstResult();
        return EntityValidator.returnOrThrowIfNull(
                list,
                "There is no list with id " + listId + " and list with task with id " + taskId
        );
    }
}
