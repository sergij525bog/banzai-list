package org.oldman.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.oldman.entities.Task;
import org.oldman.entities.entityUtils.EntityValidator;
import org.oldman.entities.enums.Priority;
import org.oldman.entities.enums.TaskCategory;
import org.oldman.repositories.ListWithTaskRepository;
import org.oldman.repositories.TaskRepository;

import java.util.List;

@ApplicationScoped
public class TasksService {
    @Inject
    TaskRepository taskRepository;

    @Inject
    ListWithTaskRepository joinTableRepository;

    public List<Task> findAllTasks() {
        return taskRepository.findAllTasks();
    }

    public Task findTaskById(Long id) {
        return taskRepository.findTaskById(id);
    }

    @Transactional
    public void saveTask(Task task) {
//        EntityValidator.validateEntityBeforeSave(task);
        taskRepository.persist(task);
    }

    public void updateTask(Long id, Task task) {
        Task entity = findTaskById(id);

        //        EntityValidator.validateEntityBeforeSave(task);
        if (task == null || task.getName() == null || task.getName().isBlank()) {
            throw new IllegalArgumentException("Updating of task with id " + id + " was terminated! Input task is " + task);
        }
        entity.setName(task.getName());
    }

    public Task findByIdJoinFetchItemList(Long id) {
        return taskRepository.findByIdJoinFetchItemList(id);
    }

//    TODO: test it
    public void deleteTaskById(Long id) {
        Task task = findByIdJoinFetchItemList(id);

        joinTableRepository.deleteByTask(id);
        taskRepository.delete(task);
    }

    public List<Task> findAllTasksByList(
            Long listId,
            TaskCategory category,
            Priority priority) {
        if (category != null && priority != null) {
            return taskRepository.getAllTasksByListIdAndCategoryAndPriority(listId, category, priority);
        }
        if (category != null) {
            return taskRepository.getAllTasksByListIdAndCategory(listId, category);
        }
        if (priority != null) {
            return taskRepository.getAllTasksByListIdAndPriority(listId, priority);
        }
        return taskRepository.getAllTasksByListId(listId);
    }

    public Task findTaskByName(String name) {
        return taskRepository.findTaskByName(name);
    }
}