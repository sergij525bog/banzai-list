package org.oldman.models.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.oldman.entities.ListWithTask;
import org.oldman.entities.Task;
import org.oldman.models.TaskDto;

@ApplicationScoped
public class ListWithTaskToDtoMapper implements IEntityToDtoMapper<Task, ListWithTask, TaskDto>{
    @Override
    public TaskDto toDto(ListWithTask list) {
        TaskDto model = new TaskDto();
        model.setId(list.getId());
        model.setName(list.getTask().getName());
        model.setDescription(list.getDescription());
        model.setStartDate(list.getStartDate());
        model.setEndDate(list.getEndDate());
        model.setPriority(list.getPriority());
        model.setTaskCategory(list.getTaskCategory());
        model.setTaskId(list.getTask().getId());
//        model.setDone(list.isDone());

        return model;
    }

    @Override
    public ListWithTask toEntity(TaskDto dto) {
        ListWithTask listWithTask = new ListWithTask();
        listWithTask.setTaskCategory(dto.getTaskCategory());
        listWithTask.setPriority(dto.getPriority());
        listWithTask.setDescription(dto.getDescription());
        listWithTask.setStartDate(dto.getStartDate());
        listWithTask.setEndDate(dto.getEndDate());
        listWithTask.setId(dto.getId());

        Task task = new Task();
        task.setId(dto.getTaskId());
        task.setName(dto.getName());
        listWithTask.setTask(task);
        return listWithTask;
    }
}
