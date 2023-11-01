package org.oldman.services;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.oldman.entities.Task;
import org.oldman.repositories.TaskRepository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class TaskServiceTest {

    @InjectMock
    TaskRepository repository;

    @Inject
    TaskService service;

    @Test
    public void itShouldRejectSavingTaskWithNullOrWhitespaceName() {
        Task task = new Task();
        task.setName(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(task));
        
        task.setName(" ");
        assertThrows(IllegalArgumentException.class, () -> service.save(task));
        
        task.setName("\t\n");
        assertThrows(IllegalArgumentException.class, () -> service.save(task));
    }

    @Test
    public void itShouldSaveTask() {
        Task task = new Task();
        task.setName("Some test name");

        assertDoesNotThrow(() -> service.save(task));
    }

    @Test
    public void itShouldThrowIfTaskNotFound() {
        assertThrows(RuntimeException.class, () -> service.findByIdJoinFetchItemList(-1L));
    }
}
