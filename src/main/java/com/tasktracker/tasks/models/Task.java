package com.tasktracker.tasks.models;

import org.springframework.lang.NonNull;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long taskId;

    @NonNull
    private String taskName;
    private String taskPreview;
    private String taskDescription;
    private Long views = 0L;

    @ManyToOne
    @JoinColumn(name = "parentTaskId")
    private Task parentTask;

    @OneToMany(mappedBy = "parentTask", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "childId")
    private List<Task> children;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "authorId")
    private Author author;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "userSelectedTheTaskId")
    private UserSelectedTheTask userSelectedTheTask;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "plannedTimeId")
    private PlannedTime plannedTime;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "actualTimeId")
    private ActualTime actualTime;

    @ElementCollection(targetClass = Status.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "taskStatus", joinColumns = @JoinColumn(name = "taskId"))
    @Enumerated(EnumType.STRING)
    private List<Status> status;

    public Task() {
    }

    public Task(@NonNull String taskName, String taskPreview, String taskDescription, Long views, Task parentTask, List<Task> children, Author author, UserSelectedTheTask userSelectedTheTask, PlannedTime plannedTime, ActualTime actualTime, List<Status> status) {
        this.taskName = taskName;
        this.taskPreview = taskPreview;
        this.taskDescription = taskDescription;
        this.views = views;
        this.parentTask = parentTask;
        this.children = children;
        this.author = author;
        this.userSelectedTheTask = userSelectedTheTask;
        this.plannedTime = plannedTime;
        this.actualTime = actualTime;
        this.status = status;
    }

    public void incrementViews() {
        this.views++;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName != null ? taskName : "";
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskPreview() {
        return taskPreview != null ? taskPreview : "";
    }

    public void setTaskPreview(String taskPreview) {
        this.taskPreview = taskPreview;
    }

    public String getTaskDescription() {
        return taskDescription != null ? taskDescription : "";
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Task getParentTask() {
        return parentTask;
    }

    public void setParentTask(Task parentTask) {
        this.parentTask = parentTask;
    }

    public PlannedTime getPlannedTime() {
        return plannedTime;
    }

    public void setPlannedTime(PlannedTime plannedTime) {
        this.plannedTime = plannedTime;
    }

    public ActualTime getActualTime() {
        return actualTime;
    }

    public void setActualTime(ActualTime actualTime) {
        this.actualTime = actualTime;
    }

    public List<Task> getChildren() {
        return children;
    }

    public void setChildren(List<Task> children) {
        this.children = children;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public UserSelectedTheTask getUserSelectedTheTask() {
        return userSelectedTheTask;
    }

    public void setUserSelectedTheTask(UserSelectedTheTask userSelectedTheTask) {
        this.userSelectedTheTask = userSelectedTheTask;
    }

    public void setStatuses(List<Status> status) {
        this.status = status;
    }

    public Status getStatus() {
        for (Status status : this.status) {
            return status;
        }
        return null;
    }

    public void setStatus(Status status) {
        List<Status> statuses = new ArrayList<>();
        statuses.add(status);
        this.status = statuses;
    }
}
