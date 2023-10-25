package org.oldman.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import org.oldman.entities.ItemList;
import org.oldman.entities.entityUtils.EntityValidator;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ItemListRepository implements PanacheRepository<ItemList> {
    public List<ItemList> findAllTItemListsJoinFetchTaskAndProduct() {
        return list("select distinct l from ItemList l " +
                "left join fetch l.listWithTasks lt " +
                "left join fetch lt.task t");
    }

    public ItemList findItemListJoinFetchTaskAndProductById(long id) {
        final ItemList list = find("select distinct l from ItemList l " +
                        "left join fetch l.listWithTasks lt " +
                        "left join fetch lt.task t " +
                        "where l.id = :listId",
                Parameters.with("listId", id)
        ).firstResult();
        return EntityValidator.returnOrThrowIfNull(list, "There is no list with id " + id);
    }

    public ItemList findItemListByIdFetchTask(long id) {
        final ItemList list = find("select l from ItemList l " +
                        "left join fetch l.listWithTasks lt " +
                        "left join fetch lt.task " +
                        "where l.id = :listId",
                Parameters.with("listId", id)
        ).firstResult();
        return EntityValidator.returnOrThrowIfNull(list, "There is no list with id " + id);
    }

    public Optional<ItemList> findItemListByIdJoinListWithTask(long id) {
        return find("select l from ItemList l " +
                "left join l.listWithTasks lt " +
                "where l.id = :id",
                Parameters.with("id", id)
                ).firstResultOptional();
    }

    public ItemList findItemListsJoinFetchTask(long listId) {
        final ItemList list = find("select distinct l from ItemList l " +
                        "left join fetch l.listWithTasks lt " +
                        "left join fetch lt.task t " +
                        "where l.id = :listId ",
                Parameters.with("listId", listId)
        ).firstResult();
        return EntityValidator.returnOrThrowIfNull(list, "There is no list with id " + listId);
    }

    public ItemList findItemListsWithTasksById(long id) {
        return find("select distinct l from ItemList l " +
                "left join fetch l.listWithTasks lt " +
                "left join fetch lt.task t " +
//                "left join lt.calendarPlanning c " +
                "where l.id = :listId",
                Parameters.with("listId", id))
                .firstResult();
    }

    public ItemList findItemListByIdWithoutAnyJoin(long listId) {
        final ItemList list = find("select l from ItemList l " +
                        "where l.id = :listId ",
                Parameters.with("listId", listId)
        ).firstResult();
        return EntityValidator.returnOrThrowIfNull(list, "There is no list with id " + listId);
    }
}
