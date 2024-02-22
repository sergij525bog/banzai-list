package org.oldman.models;

import org.oldman.entities.enums.Priority;
import org.oldman.entities.enums.TaskCategory;

import java.time.LocalDateTime;

public class TaskDto implements Dto{
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Priority priority;
    private TaskCategory taskCategory;
    private Long taskId;
    private boolean done;

    public TaskDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public TaskCategory getTaskCategory() {
        return taskCategory;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setTaskCategory(TaskCategory taskCategory) {
        this.taskCategory = taskCategory;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "TaskModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", priority=" + priority +
                ", taskCategory=" + taskCategory +
                ", done=" + done +
                '}';
    }

    // for cycled tasks; if task isn't cycled, then period is 0
//    @Min(value = 0, message = "period cannot be less than 0")
//    private int period = 0;

//    @JsonIgnore
//    @ElementCollection
//    @CollectionTable(name="calendar_planned_dates", joinColumns=@JoinColumn(name="task_id"))
//    @Column(name="planned_dates")
//    private List<LocalDateTime> calendarPlanning = new ArrayList<>();
}
