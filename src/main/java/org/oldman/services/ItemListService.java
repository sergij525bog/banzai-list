package org.oldman.services;

//import com.github.dockerjava.api.exception.ConflictException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.oldman.entities.ItemList;
import org.oldman.entities.ListWithTask;
import org.oldman.entities.Task;
import org.oldman.entities.entityUtils.EntityValidator;
import org.oldman.entities.enums.Priority;
import org.oldman.entities.enums.TaskCategory;
import org.oldman.repositories.ItemListRepository;
import org.oldman.repositories.ListWithTaskRepository;
import org.oldman.repositories.TaskRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@ApplicationScoped
public class ItemListService {
    @Inject
    ItemListRepository itemListRepository;

    @Inject
    TaskRepository taskRepository;

    @Inject
    ListWithTaskRepository joinTableRepository;


    public List<ItemList> findAllItemListsWithTasksAndProducts() {
        return itemListRepository.findAllTItemListsJoinFetchTaskAndProduct();
    }

    public ItemList findItemListJoinFetchTaskAndProductById(long id) {
        return itemListRepository.findItemListJoinFetchTaskAndProductById(id);
    }

    public ItemList findItemListsJoinFetchTask(long id) {
        return itemListRepository.findItemListsJoinFetchTask(id);
    }

    public void saveItemList(ItemList list) {
         EntityValidator.validateEntityBeforeSave(list);
        itemListRepository.persist(list);
    }

    public void addTaskToList(long listId, long taskId) {
        final ItemList list = findItemListsJoinFetchTask(listId);
        final Task task = taskRepository.findTaskById(taskId);

        final boolean listContainsTask = list
                .getListWithTasks()
                .stream()
                .anyMatch(lt -> lt.getTask().equals(task));

        if (listContainsTask) {
//            throw new ConflictException("List already contains this task");
            throw new IllegalArgumentException("List already contains this task");
        }
//        Task task = Instancio.of(Task.class)
//                .ignore(field(Task.class, "id"))
//                .create();

        final ListWithTask listWithTask = new ListWithTask();
        listWithTask.setTask(task);
        listWithTask.setItemList(list);
        joinTableRepository.persist(listWithTask);
    }

    public void deleteTaskFromList(long listId, long taskId) {
        joinTableRepository.delete(getListWithTask(listId, taskId));
    }

    public int findAllTItemListsJoinFetchTaskAndProductCount() {
        return itemListRepository.findAllTItemListsJoinFetchTaskAndProduct().size();
    }

    public int getTaskCount(long id) {
        return itemListRepository
                .findItemListByIdFetchTask(id)
                .getListWithTasks()
                .size();
    }

    @Transactional
    public void clearList(long listId) {
        //        TODO: this line is only for validation list is in db. It should be replaced with less expensive operation
        final ItemList itemList = itemListRepository.findItemListByIdFetchTask(listId);

        joinTableRepository.deleteAllByItemList(listId);
    }

    @Transactional
    public void changeTaskPriority(long listId, long taskId, Priority priority) {
        throwConflictExceptionByPredicate(priority, Objects::isNull, "You don't pass a new priority");

        final ListWithTask listWithTask = getListWithTask(listId, taskId);
        listWithTask.setPriority(priority);
    }

    @Transactional
    public void changeTaskCategory(long listId, long taskId, TaskCategory category) {
        throwConflictExceptionByPredicate(category, Objects::isNull, "You don't pass a new category");

        final ListWithTask listWithTask = getListWithTask(listId, taskId);
        listWithTask.setTaskCategory(category);
    }

    private ListWithTask getListWithTask(long listId, long taskId) {
        final ItemList list = findItemListsJoinFetchTask(listId);
        final Task task = taskRepository.findTaskById(taskId);

        final Optional<ListWithTask> listWithTask = list
                .getListWithTasks()
                .stream()
                .filter(lt -> lt.getTask().equals(task))
                .findFirst();

        if (listWithTask.isEmpty()) {
            throw new IllegalArgumentException("List does not contain task with id " + taskId);
        }

        return listWithTask.get();
    }

    private <T> void throwConflictExceptionByPredicate(T object, Predicate<T> predicate, String errorMessage) {
        if (predicate.test(object)) {
//            throw new ConflictException(errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }
}
