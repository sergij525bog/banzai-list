package org.oldman.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import org.oldman.entities.ListWithProduct;
import org.oldman.entities.ListWithTask;
import org.oldman.entities.entityUtils.EntityValidator;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ListWithTaskRepository implements PanacheRepository<ListWithTask> {
    public ListWithTask findById(Long id) {
        final ListWithTask listWithTask = find("select lt from ListWithTask lt " +
                "where lt.id = :id",
                Parameters.with("id", id))
                .firstResult();
        return EntityValidator.returnOrThrowIfNull(
                listWithTask,
                "There is no list with task with id " + id
        );
    }
    public List<ListWithTask> findAllByListFetchTask(Long listId) {
        return list("select lt from ListWithTask lt " +
                        "left join fetch lt.task " +
                        "left join lt.taskList tl " +
                        "where tl.id = :listId ",
                Parameters.with("listId", listId)
        );
    }

    public void updateById(final Long id, final Map<String, Object> updateParams) {
        String query = "update ListWithTask lt set";
        String where = "where lt.id = :id";
        Parameters parameters = Parameters.with("id", id);
        String tableAlias = "lt";
        try {
            QueryEditor editor = QueryEditor.getEditor(query, parameters, tableAlias, updateParams);
            update(editor.getQuery() + where, editor.getParameters());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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

    public void checkContainedInList(Long listId, Long listWithTaskId) {
        long count = count(
                "id = :listWithTaskId and taskList.id = :listId",
                Parameters.with("listWithTaskId", listWithTaskId)
                        .and("listId", listId)
        );

//        System.out.println(count);
        if (count == 0L) {
            throw new NotFoundException("List does not contain list with task with id " + listWithTaskId);
        }
    }

    public ListWithTask findByIdAndList(Long listWithTaskId, Long listId) {
        final ListWithTask ListWithTask = find("select lt from ListWithTask lt " +
                        "where lt.id = :ListWithTaskId " +
                        "and lt.productList.id = :listId",
                Parameters.with("ListWithTaskId", listWithTaskId)
                        .and("listId", listId))
                .firstResult();
        return EntityValidator.returnOrThrowIfNull(
                ListWithTask,
                "Product list doe not contain list with task with id " + listWithTaskId
        );
    }
}
