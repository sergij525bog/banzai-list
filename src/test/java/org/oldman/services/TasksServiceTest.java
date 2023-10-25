package org.oldman.services;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.oldman.entities.Task;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class TasksServiceTest {

    @Inject
    TasksService service;

    @Test
    public void itShouldRejectSavingTaskWithNullOrWhitespaceName() {
        Task task = new Task();
        task.setName(null);
        assertThrows(RuntimeException.class, () -> service.saveTask(task));
        
        task.setName(" ");
        assertThrows(RuntimeException.class, () -> service.saveTask(task));
        
        task.setName("\t\n");
        assertThrows(RuntimeException.class, () -> service.saveTask(task));
    }

    @Test
    public void itShouldSaveTask() {
        Task task = new Task();
        task.setName("name1");

        assertDoesNotThrow(() -> service.saveTask(task));
        System.out.println(task.getId());
    }

    @Test
    public void itShouldThrowIfTaskNotFound() {
        assertThrows(NotFoundException.class, () -> service.findByIdJoinFetchItemList(-1L));
    }
}
