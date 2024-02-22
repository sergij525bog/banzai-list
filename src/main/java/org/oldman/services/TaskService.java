package org.oldman.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.oldman.entities.ListWithTask;
import org.oldman.entities.Task;
import org.oldman.entities.entityUtils.EntityValidator;
import org.oldman.entities.enums.Priority;
import org.oldman.entities.enums.TaskCategory;
import org.oldman.models.TaskDto;
import org.oldman.models.mappers.ListWithTaskToDtoMapper;
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

    @Inject
    ListWithTaskToDtoMapper dtoMapper;

    public List<Task> findAll() {
        return taskRepository.findAllTasks();
    }

    public TaskDto findDtoById(Long id) {
        return taskRepository.findDtoById(id);
    }

    @Transactional
    public void save(TaskDto taskDto) {
        ListWithTask listWithTask = dtoMapper.toEntity(taskDto);
        EntityValidator.validateEntityBeforeSave(listWithTask);
        Task task = listWithTask.getTask();
        EntityValidator.validateEntityBeforeSave(task);
        taskRepository.persist(task);
        joinTableRepository.persist(listWithTask);
    }

    public void update(Long id, Task task) {
        Task entity = taskRepository.findById(id);

        //        EntityValidator.validateEntityBeforeSave(task);
        if (task == null || task.getName() == null || task.getName().isBlank()) {
            throw new IllegalArgumentException("Updating of task with id " + id + " was terminated! Input task is " + task);
        }
        entity.setName(task.getName());
    }

    public Task findByIdJoinFetchTaskList(Long id) {
        return taskRepository.findByIdJoinFetchTaskList(id);
    }

//    TODO: test it
    public void delete(Long id) {
        Task task = findByIdJoinFetchTaskList(id);

        joinTableRepository.deleteByTask(id);
        taskRepository.delete(task);
    }

    public List<TaskDto> findAllByList(
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
        return dtoMapper.toDtoList(taskStream);
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