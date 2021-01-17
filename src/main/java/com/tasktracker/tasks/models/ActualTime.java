package com.tasktracker.tasks.models;

import javax.persistence.*;


@Entity
@Table(name = "actualTime")
public class ActualTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long actualTimesId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "timerId")
    private Timer timer;// подробная информация о фактическом времени

    @OneToOne(mappedBy = "actualTime")
    @JoinColumn(name = "taskId")
    private Task task;

    public ActualTime() {
    }

    public ActualTime(Timer timer, Task task) {
        this.timer = timer;
        this.task = task;
    }

    public Long getActualTimesId() {
        return actualTimesId;
    }

    public void setActualTimesId(Long actualTimesId) {
        this.actualTimesId = actualTimesId;
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
