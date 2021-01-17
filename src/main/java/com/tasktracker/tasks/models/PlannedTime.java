package com.tasktracker.tasks.models;

import javax.persistence.*;

@Entity
@Table(name = "plannedTime")
public class PlannedTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long plannedTimesId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "timerId")
    private Timer timer;// подробная информация о планируемом времени

    @OneToOne(mappedBy = "plannedTime")
    @JoinColumn(name = "taskId")
    private Task task;

    public PlannedTime() {
    }

    public PlannedTime(Timer timer, Task task) {
        this.timer = timer;
        this.task = task;
    }


    public Long getPlannedTimesId() {
        return plannedTimesId;
    }

    public void setPlannedTimesId(Long plannedTimesId) {
        this.plannedTimesId = plannedTimesId;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
