package org.oldman.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.oldman.entities.ListWithTask;
import org.oldman.repositories.ListWithTaskRepository;

@ApplicationScoped
public class ListWithTaskService {
    @Inject
    ListWithTaskRepository listWithTaskRepository;

    public void updateDescription(Long listId, Long taskId, String description) {
        updateDescription(findByListAndTask(listId, taskId), description);
    }

    public void updateDescription(ListWithTask listWithTask, String description) {
        if (description == null) {
            throw new IllegalArgumentException(
                    "ListWithTask with id " +
                    listWithTask.getId() +
                    " was not updated! Description is null!"
            );
        }
        listWithTask.setDescription(description);
    }

    public ListWithTask findByListAndTask(Long listId, Long taskId) {
        return listWithTaskRepository.findByListAndTask(listId, taskId);
    }
}
