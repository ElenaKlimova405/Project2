package com.tasktracker.tasks.additional_classes;

import com.tasktracker.tasks.models.Task;

public class Triple {
    String number = "";
    Task task;
    boolean isLeaf = false;

    public Triple(String number, Task task, boolean isLeaf) {
        this.number = number;
        this.task = task;
        this.isLeaf = isLeaf;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }
}
