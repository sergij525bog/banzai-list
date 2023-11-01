package org.oldman.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.oldman.entities.enums.Priority;
import org.oldman.entities.enums.TaskCategory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "lists_with_tasks")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class ListWithTask {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id")
    private TaskList taskList;

    private String description;

    @FutureOrPresent(message = "you cannot start a task in the past")
    private LocalDateTime startDate;

    @Future(message = "you cannot finish a task in the past")
    private LocalDateTime endDate = null;

    private Priority priority = Priority.LOW;
    private TaskCategory taskCategory = TaskCategory.NONE;
//    private boolean done = false;

    // for cycled tasks; if task isn't cycled, then period is 0
//    @Min(value = 0, message = "period cannot be less than 0")
//    private int period = 0;

    @JsonIgnore
    @ElementCollection
    @CollectionTable(name="calendar_planned_dates", joinColumns=@JoinColumn(name="task_id"))
    @Column(name="planned_dates")
    private List<LocalDateTime> calendarPlanning = new ArrayList<>();

    @Override
    public String toString() {
        return "ListWithTask{" +
                "id=" + id +
                ", task=" + task +
                ", taskList=" + taskList +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", priority=" + priority +
                ", taskCategory=" + taskCategory +
//                ", done=" + done +
//                ", calendarPlanning=" + calendarPlanning +
                '}';
    }
}
