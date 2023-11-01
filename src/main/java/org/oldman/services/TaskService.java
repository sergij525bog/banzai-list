package org.oldman.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.oldman.entities.Task;
import org.oldman.entities.enums.Priority;
import org.oldman.entities.enums.TaskCategory;
import org.oldman.models.TaskModel;
import org.oldman.repositories.ListWithTaskRepository;
import org.oldman.repositories.TaskRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@ApplicationScoped
public class TaskService {
    @Inject
    TaskRepository taskRepository;

    @Inject
    ListWithTaskRepository joinTableRepository;

    public List<Task> findAll() {
        return taskRepository.findAllTasks();
    }

    public Task findById(Long id) {
        return taskRepository.findTaskById(id);
    }

    @Transactional
    public void save(Task task) {
//        EntityValidator.validateEntityBeforeSave(task);
        taskRepository.persist(task);
    }

    public void update(Long id, Task task) {
        Task entity = findById(id);

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
    public void delete(Long id) {
        Task task = findByIdJoinFetchItemList(id);

        joinTableRepository.deleteByTask(id);
        taskRepository.delete(task);
    }

    public List<TaskModel> findAllByList(
            Long listId,
            TaskCategory category,
            Priority priority,
            Boolean done,
            List<String> sortBy,
            String sortOrder) {
        Map<String, Object> filters = new HashMap<>();
        filters.put("taskCategory", category);
        filters.put("priority", priority);
//        filters.put("done", done);

        Stream<Task> taskStream = taskRepository.getAllByListWithFiltersAsStream(listId, filters, sortBy, sortOrder);
        return TaskModel.toModelList(taskStream);
    }

    public Task findByName(String name) {
        return taskRepository.findTaskByName(name);
    }

    public Long count() {
        return taskRepository.count();
    }

    public void checkExists() {
        Long id = 1L;
        taskRepository.checkExistsById(id);
        System.out.println("Check with full select");
//        taskRepository.checkExistsByIdWithFullSelect(id);
    }
}