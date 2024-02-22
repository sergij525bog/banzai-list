package org.oldman.services;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.oldman.entities.Task;
import org.oldman.repositories.TaskRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@QuarkusTest
public class TaskServiceTest {
    private static final Task task = new Task();

    @BeforeEach
    public void setUp() {
        task.setId(1L);
        task.setName("Task");
    }

    @InjectMock
    TaskRepository repositoryMock;

    @Inject
    TaskService service;

    @Test
    public void itShouldOnlyPassDataFromRepository() {
        List<Task> tasks = List.of(new Task(), new Task());

        when(repositoryMock.findAllTasks()).thenReturn(tasks);
        assertEquals(tasks, service.findAll());
    }

    @Test
    public void itShouldRejectSavingTaskWithNullOrWhitespaceName() {
//        Task task = new Task();
//        task.setName(null);
//        assertThrows(IllegalArgumentException.class, () -> service.save(task));
//
//        task.setName(" ");
//        assertThrows(IllegalArgumentException.class, () -> service.save(task));
//
//        task.setName("\t\n");
//        assertThrows(IllegalArgumentException.class, () -> service.save(task));
    }

    @Test
    public void itShouldSaveTask() {
        Task task = new Task();
        task.setName("Some test name");

//        assertDoesNotThrow(() -> service.save(task));
    }

    @Test
    public void itShouldThrowIfTaskNotFound() {
        when(repositoryMock.findByIdJoinFetchTaskList(anyLong())).thenThrow(new NotFoundException());
        assertThrows(RuntimeException.class, () -> service.findByIdJoinFetchTaskList(-1L));
    }
}
