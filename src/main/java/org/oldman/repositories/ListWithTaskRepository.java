package org.oldman.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import org.oldman.entities.ListWithTask;
import org.oldman.entities.entityUtils.EntityValidator;

@ApplicationScoped
public class ListWithTaskRepository implements PanacheRepository<ListWithTask> {
    public ListWithTask findByListAndTask(long listId, long taskId) {
        final ListWithTask list = find("select lt from ListWithTask lt " +
                        "where lt.itemList.id = :listId " +
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

    public void deleteByTask(long taskId) {
        delete("delete from ListWithTask lt " +
                "where lt.task.id = :taskId",
                Parameters.with("taskId", taskId));
    }

    public void deleteAllByItemList(long listId) {
        delete("delete from ListWithTask lt " +
                "where lt.itemList.id = :listId",
                Parameters.with("listId", listId));
    }
}
