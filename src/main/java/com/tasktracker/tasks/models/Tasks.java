package com.tasktracker.tasks.models;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tasks")
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long taskId;
    @NonNull
    private String taskName;
    private String taskPreview;
    private String taskDescription;
    private Long views = 0L;
    @OneToOne(fetch = FetchType.EAGER)
    /*@JsonIgnore*/
    @JoinColumn(name = "parentTaskId")
    private Tasks parentTask;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authorId")
    //@NonNull
    /*@JsonIgnore*/
    private Users author;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userSelectedTheTaskId")
    /*@JsonIgnore*/
    private Users userSelectedTheTask;

    @OneToOne(/*fetch = FetchType.EAGER, */cascade = CascadeType.ALL/*, orphanRemoval = true*/)
    @JoinColumn(name = "plannedTimeId")
    private Timer plannedTime;
    @OneToOne(/*fetch = FetchType.EAGER, */cascade = CascadeType.ALL/*, orphanRemoval = true*/)
    @JoinColumn(name = "actualTimeId")
    private Timer actualTime;


    @ElementCollection(targetClass = Statuses.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "taskStatus", joinColumns = @JoinColumn(name = "taskId"))
    @Enumerated(EnumType.STRING)
    private Set<Statuses> status;



    public Tasks() {
    }

    public Tasks(@NonNull String taskName, String taskPreview, String taskDescription, Long views, Tasks parentTask, Users author, Users userSelectedTheTask, Timer plannedTime, Timer actualTime, Statuses status) {
        this.taskName = taskName;
        this.taskPreview = taskPreview;
        this.taskDescription = taskDescription;
        this.views = views;
        this.parentTask = parentTask;
        this.author = author;
        this.userSelectedTheTask = userSelectedTheTask;
        this.plannedTime = plannedTime;
        this.actualTime = actualTime;
        this.status = Collections.singleton(status);
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

    public Tasks getParentTask() {
        return parentTask;
    }

    public void setParentTask(Tasks parentTask) {
        this.parentTask = parentTask;
    }

    public Users getAuthor() {
        return author;
    }

    public void setAuthor(Users author) {
        this.author = author;
    }

    public Users getUserSelectedTheTask() {
        return userSelectedTheTask;
    }

    public void setUserSelectedTheTask(Users userSelectedTheTask) {
        this.userSelectedTheTask = userSelectedTheTask;
    }

    public Timer getPlannedTime() {
        return plannedTime;
    }

    public void setPlannedTime(Timer plannedTime) {
        this.plannedTime = plannedTime;
    }

    public Timer getActualTime() {
        return actualTime;
    }

    public void setActualTime(Timer actualTime) {
        this.actualTime = actualTime;
    }

    public Statuses getStatus() {
        for (Statuses status : this.status) {
            return status;
        }
        return null;
    }

    public void setStatus(Statuses status) {
        Set<Statuses> statuses = new HashSet<>();
        statuses.add(status);
        this.status = statuses;
    }
}
