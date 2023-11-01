package org.oldman.models;

import org.oldman.entities.ListWithTask;
import org.oldman.entities.Task;
import org.oldman.entities.enums.Priority;
import org.oldman.entities.enums.TaskCategory;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TaskModel {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Priority priority;
    private TaskCategory taskCategory;
//    private Long taskId;
    private boolean done;

    public TaskModel() {
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

//    public Long getTaskId() {
//        return taskId;
//    }
//
//    public void setTaskId(Long taskId) {
//        this.taskId = taskId;
//    }

    public static TaskModel toModel(ListWithTask list) {
        TaskModel model = new TaskModel();
        model.setId(list.getId());
        model.setName(list.getTask().getName());
        model.setDescription(list.getDescription());
        model.setStartDate(list.getStartDate());
        model.setEndDate(list.getEndDate());
        model.setPriority(list.getPriority());
        model.setTaskCategory(list.getTaskCategory());
//        model.setTaskId(list.getTask().getId());
//        model.setDone(list.isDone());

        return model;
    }

    public static Stream<TaskModel> toModelStream(Collection<ListWithTask> listWithTaskCollection) {
        return listWithTaskCollection.stream().map(TaskModel::toModel);
    }

    public static Stream<TaskModel> toModelStream(Stream<Task> taskStream) {
        return taskStream.flatMap(task -> task.getListWithTasks().stream()).map(TaskModel::toModel);
    }

    public static List<TaskModel> toModelList(Collection<ListWithTask> listWithTaskCollection) {
        return toModelStream(listWithTaskCollection).collect(Collectors.toList());
    }

    public static List<TaskModel> toModelList(Stream<Task> taskStream) {
        return toModelStream(taskStream).collect(Collectors.toList());
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
