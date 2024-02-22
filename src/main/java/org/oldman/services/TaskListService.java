package org.oldman.services;

//import com.github.dockerjava.api.exception.ConflictException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.oldman.entities.ListWithTask;
import org.oldman.entities.Task;
import org.oldman.entities.TaskList;
import org.oldman.entities.entityUtils.EntityValidator;
import org.oldman.entities.enums.Priority;
import org.oldman.entities.enums.TaskCategory;
import org.oldman.models.TaskDto;
import org.oldman.models.mappers.ListWithTaskToDtoMapper;
import org.oldman.repositories.ListWithTaskRepository;
import org.oldman.repositories.TaskListRepository;
import org.oldman.repositories.TaskRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class TaskListService {
    @Inject
    TaskListRepository taskListRepository;

    @Inject
    TaskRepository taskRepository;

    @Inject
    ListWithTaskRepository joinTableRepository;

    @Inject
    ListWithTaskToDtoMapper dtoMapper;

    public List<TaskList> findAllTaskListsFetchTask() {
        return taskListRepository.findAllTaskListsFetchTask();
    }

    public TaskList findByIdFetchTask(long id) {
        return taskListRepository.findByIdFetchTask(id);
    }

    public void saveItemList(TaskList list) {
         EntityValidator.validateEntityBeforeSave(list);
         taskListRepository.persist(list);
    }

    public void addTaskToList(long listId, long taskId) {
        final TaskList list = findByIdFetchTask(listId);
        final Task task = taskRepository.findById(taskId);

        final boolean listContainsTask = list
                .getListWithTasks()
                .stream()
                .anyMatch(lt -> lt.getTask().getId().equals(taskId));

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

    public void deleteTaskFromList(long listId, long listWithTaskId) {
        joinTableRepository.delete(getListWithTask(listId, listWithTaskId));
    }

    public int findTaskListsCount() {
        return taskListRepository.findAllLists().size();
    }

//    TODO: replace with db request to get count
    public int getTaskCount(long id) {
        return findByIdFetchTask(id)
                .getListWithTasks()
                .size();
    }

    @Transactional
    public void clearList(long listId) {
        taskListRepository.checkExistsById(listId);

        joinTableRepository.deleteAllByItemList(listId);
    }

    @Transactional
    public void changeTaskParameters(long listId, long listWithTaskId, Priority priority, TaskCategory category) {
        if (priority == null && category == null) {
            throw new IllegalArgumentException("You don't pass new task parameters");
        }

        taskListRepository.checkExistsById(listId);
        joinTableRepository.checkContainedInList(listId, listWithTaskId);

        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("priority", priority);
        updateParams.put("taskCategory", category);

        joinTableRepository.updateById(listWithTaskId, updateParams);
    }

    @Transactional
    public void update(long id, TaskList taskList) {
        final TaskList entity = taskListRepository.findById(id);
        //        EntityValidator.validateEntityBeforeSave(taskList);

        entity.setName(taskList.getName());
    }

    @Transactional
    public void deleteList(long id) {
        taskListRepository.checkExistsById(id);
        joinTableRepository.deleteAllByTaskList(id);
        taskListRepository.deleteById(id);
    }

    public List<TaskDto> getTaskModels(long id) {
        final List<ListWithTask> listWithTasks = joinTableRepository.findAllByListFetchTask(id);
        return dtoMapper.toDtoList(listWithTasks);
    }

    public TaskDto getTaskModel(long listId, long listWithTaskId) {
        final ListWithTask listWithTask = joinTableRepository.findByIdAndListFetchTask(listId, listWithTaskId);
        return dtoMapper.toDto(listWithTask);
    }

    @Transactional
    public void changeTaskStatus(long listId, long listWithTaskId) {
        final ListWithTask listWithTask = joinTableRepository.findByIdAndListFetchTask(listId, listWithTaskId);
//        listWithTask.setDone(!listWithTask.isDone());
    }

    @Transactional
    public void moveToOtherList(long listId, long listWithTaskId, long newListId) {
        final ListWithTask listWithTask = joinTableRepository.findByIdAndListFetchTask(listId, listWithTaskId);
        final TaskList newList = findByIdFetchTask(newListId);
        listWithTask.setTaskList(newList);
        newList.getListWithTasks().add(listWithTask);
    }

    private ListWithTask getListWithTask(long listId, long listWithTaskId) {
        taskListRepository.checkExistsById(listId);

        return joinTableRepository.findByIdAndList(listWithTaskId, listId);
    }
}
