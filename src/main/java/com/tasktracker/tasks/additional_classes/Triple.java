package com.tasktracker.tasks.additional_classes;

import com.tasktracker.tasks.models.Tasks;

public class Triple {
    String number = "";
    Tasks task;
    boolean isLeaf = false;

    public Triple(String number, Tasks task, boolean isLeaf) {
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

    public Tasks getTask() {
        return task;
    }

    public void setTask(Tasks task) {
        this.task = task;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }
}
