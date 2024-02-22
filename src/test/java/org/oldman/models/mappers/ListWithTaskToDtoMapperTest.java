package org.oldman.models.mappers;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.oldman.entities.ListWithTask;
import org.oldman.entities.Task;
import org.oldman.models.TaskDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class ListWithTaskToDtoMapperTest {
    @Inject
    ListWithTaskToDtoMapper mapper;

    @Test
    public void itShouldMapListWithTaskSuccessfully() {
        ListWithTask listWithTask = Instancio.create(ListWithTask.class);

        TaskDto dto = mapper.toDto(listWithTask);
        assertTrue(equals(listWithTask, dto));
    }

    @Test
    public void itShouldMapDtoSuccessfully() {
        TaskDto dto = Instancio.create(TaskDto.class);

        ListWithTask entity = mapper.toEntity(dto);
        assertTrue(equals(entity, dto));
    }

    @Test
    public void itShouldMapTaskSuccessfully() {
        Task task = Instancio.create(Task.class);
        Set<ListWithTask> listWithTasks = Instancio.ofSet(ListWithTask.class)
                .size(3)
                .set(field(ListWithTask::getTask), task)
                .create();
        task.setListWithTasks(listWithTasks);

        List<TaskDto> taskDtos = mapper.toDtoList(task);

        List<ListWithTask> list = new ArrayList<>(listWithTasks);
        assertEquals(list.size(), taskDtos.size());
        for (int i = 0; i < list.size(); i++) {
            assertTrue(equals(list.get(i), taskDtos.get(i)));
        }
    }

    @Test
    public void itShouldMapListWithTaskListSuccessfully() {
        List<ListWithTask> listWithTasks = Instancio.createList(ListWithTask.class);

        List<TaskDto> taskDtos = mapper.toDtoList(listWithTasks);

        assertEquals(listWithTasks.size(), taskDtos.size());
        for (int i = 0; i < listWithTasks.size(); i++) {
            assertTrue(equals(listWithTasks.get(i), taskDtos.get(i)));
        }
    }

    @Test
    public void itShouldMapTasksSuccessfully() {
        List<Task> tasks = Instancio.ofList(Task.class)
                .size(3)
                .set(field(Task::getListWithTasks), null)
                .create();
        List<ListWithTask> listWithTasks = tasks.stream().map(task -> {
                    Set<ListWithTask> list = Instancio.ofSet(ListWithTask.class)
                            .size(2)
                            .set(field(ListWithTask::getTask), task)
                            .create();
                    task.setListWithTasks(list);
                    return list;
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<TaskDto> taskDtos = mapper.toDtoList(tasks.stream());

        assertEquals(listWithTasks.size(), taskDtos.size());
        for (int i = 0; i < listWithTasks.size(); i++) {
            assertTrue(equals(listWithTasks.get(i), taskDtos.get(i)));
        }
    }

    @Test
    public void itShouldMapTaskDtoListSuccessfully() {
        List<TaskDto> taskDtos = Instancio.createList(TaskDto.class);

        List<ListWithTask> listWithTasks = mapper.toEntityList(taskDtos);

        assertEquals(listWithTasks.size(), taskDtos.size());
        for (int i = 0; i < listWithTasks.size(); i++) {
            assertTrue(equals(listWithTasks.get(i), taskDtos.get(i)));
        }
    }

    private boolean equals(ListWithTask listWithTask, TaskDto taskDto) {
        return listWithTask.getId().equals(taskDto.getId()) &&
                listWithTask.getTask().getName().equals(taskDto.getName()) &&
                listWithTask.getDescription().equals(taskDto.getDescription()) &&
                listWithTask.getTaskCategory() == taskDto.getTaskCategory() &&
                listWithTask.getPriority() == taskDto.getPriority() &&
                listWithTask.getStartDate().equals(taskDto.getStartDate()) &&
                listWithTask.getEndDate().equals(taskDto.getEndDate()) &&
                listWithTask.getTask().getId().equals(taskDto.getTaskId());
    }
}
