package com.tasktracker.tasks.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String task_name;

    private Long parent_task_id;

    private int views;

    public Task() {
    }

    public Task(String task_name, Long parent_task_id) {
        this.task_name = task_name;
        this.parent_task_id = parent_task_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public Long getParent_task_id() {
        return parent_task_id;
    }

    public void setParent_task_id(Long parent_task_id) {
        this.parent_task_id = parent_task_id;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
