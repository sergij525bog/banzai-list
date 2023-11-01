package org.oldman.services;

//import com.github.dockerjava.api.exception.ConflictException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.oldman.entities.ListWithTask;
import org.oldman.entities.Task;
import org.oldman.entities.TaskList;
import org.oldman.entities.entityUtils.EntityValidator;
import org.oldman.entities.enums.Priority;
import org.oldman.entities.enums.TaskCategory;
import org.oldman.models.TaskModel;
import org.oldman.repositories.ListWithTaskRepository;
import org.oldman.repositories.TaskListRepository;
import org.oldman.repositories.TaskRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@ApplicationScoped
public class TaskListService {
    @Inject
    TaskListRepository taskListRepository;

    @Inject
    TaskRepository taskRepository;

    @Inject
    ListWithTaskRepository joinTableRepository;


    public List<TaskList> findAllTaskListsFetchTask() {
        return taskListRepository.findAllTaskListsFetchTask();
    }

    public TaskList findByIdFetchTask(Long id) {
        return taskListRepository.findByIdFetchTask(id);
    }

    public void saveItemList(TaskList list) {
         EntityValidator.validateEntityBeforeSave(list);
        taskListRepository.persist(list);
    }

    public void addTaskToList(Long listId, Long taskId) {
        final TaskList list = findByIdFetchTask(listId);
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
        listWithTask.setTaskList(list);
        joinTableRepository.persist(listWithTask);
    }

    public void deleteTaskFromList(Long listId, Long taskId) {
        joinTableRepository.delete(getListWithTask(listId, taskId));
    }

    public int findItemListsCount() {
        return taskListRepository.findAllLists().size();
    }

//    TODO: replace with db request to get count
    public int getTaskCount(Long id) {
        return taskListRepository
                .findItemListByIdFetchTask(id)
                .getListWithTasks()
                .size();
    }

    @Transactional
    public void clearList(Long listId) {
        taskListRepository.checkExistsById(listId);

        joinTableRepository.deleteAllByItemList(listId);
    }

    @Transactional
    public void changeTaskPriority(Long listId, Long taskId, Priority priority) {
        if (priority == null) {
            throw new IllegalArgumentException("You don't pass a new priority");
        }

        final ListWithTask listWithTask = getListWithTask(listId, taskId);
        listWithTask.setPriority(priority);
    }

    @Transactional
    public void changeTaskCategory(Long listId, Long taskId, TaskCategory category) {
        if (category == null) {
            throw new IllegalArgumentException("You don't pass a new category");
        }

        final ListWithTask listWithTask = getListWithTask(listId, taskId);
        listWithTask.setTaskCategory(category);
    }

    private ListWithTask getListWithTask(Long listId, Long taskId) {
        final TaskList list = findByIdFetchTask(listId);
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

    @Transactional
    public void update(Long id, TaskList taskList) {
        final TaskList entity = taskListRepository.findById(id);
        //        EntityValidator.validateEntityBeforeSave(taskList);

        entity.setName(taskList.getName());
    }

    @Transactional
    public void deleteList(Long id) {
        final TaskList productList = findByIdFetchTask(id);
        joinTableRepository.deleteAllByTaskList(id);
        taskListRepository.delete(productList);
    }

    public List<TaskModel> getTaskModels(Long id) {
        final List<ListWithTask> listWithTasks = joinTableRepository.findAllByListFetchTask(id);
        return TaskModel.toModelList(listWithTasks);
    }

    public TaskModel getTaskModel(Long listId, Long listWithTaskId) {
        final ListWithTask listWithTask = joinTableRepository.findByIdAndListFetchTask(listId, listWithTaskId);
        return TaskModel.toModel(listWithTask);
    }

    @Transactional
    public void changeTaskStatus(Long listId, Long listWithTaskId) {
        final ListWithTask listWithTask = joinTableRepository.findByIdAndListFetchTask(listId, listWithTaskId);
//        listWithTask.setDone(!listWithTask.isDone());
    }

    @Transactional
    public void moveToOtherList(Long listId, Long listWithTaskId, Long newListId) {
        final ListWithTask listWithTask = joinTableRepository.findByIdAndListFetchTask(listId, listWithTaskId);
        final TaskList newList = taskListRepository.findByIdFetchTask(newListId);
        listWithTask.setTaskList(newList);
        newList.getListWithTasks().add(listWithTask);
    }
}
