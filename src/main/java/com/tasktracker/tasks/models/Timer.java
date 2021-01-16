package com.tasktracker.tasks.models;

import javax.persistence.*;

@Entity
@Table(name = "timer")
public class Timer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long timerId;

    private int days = 0;
    private int hours = 0;
    private int minutes = 0;

    @OneToOne(/*fetch = FetchType.EAGER*/)
    @JoinColumn(name = "taskId")
    private Tasks task;

    public Timer() {
    }

    public Timer(int days, int hours, int minutes, Tasks task) {
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.task = task;
    }

    public Timer(int days, int hours, int minutes) {
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
    }

    public Long getTimerId() {
        return timerId;
    }

    public void setTimerId(Long timerId) {
        this.timerId = timerId;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public Tasks getTask() {
        return task;
    }

    public void setTask(Tasks task) {
        this.task = task;
    }

    public void addTime(int hours, int minutes) {
        int d = 0;
        this.minutes += minutes;
        d = this.minutes / 60;
        this.minutes %= 60;

        this.hours += hours + d;
        d = this.hours / 24;
        this.hours %= 24;

        this.days += d;
    }

    public void addTime(int days, int hours, int minutes) {
        int d = 0;
        this.minutes += minutes;
        d = this.minutes / 60;
        this.minutes %= 60;

        this.hours += hours + d;
        d = this.hours / 24;
        this.hours %= 24;

        this.days += days + d;
    }

    public long getTimeAsMinutes() {
        return this.getMinutes() + this.getHours() * 60 + this.getDays() * 24 * 60;
    }
}
