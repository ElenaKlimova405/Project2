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

    @OneToOne(mappedBy = "timer")
    @JoinColumn(name = "plannedTimesId")
    private PlannedTime plannedTime;

    @OneToOne(mappedBy = "timer")
    @JoinColumn(name = "actualTimesId")
    private ActualTime actualTime;

    public Timer() {
    }

    public Timer(int days, int hours, int minutes) {
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
    }

    public Timer(int days, int hours, int minutes, PlannedTime plannedTime, ActualTime actualTime) {
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.plannedTime = plannedTime;
        this.actualTime = actualTime;
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

    public PlannedTime getPlannedTimes() {
        return plannedTime;
    }

    public void setPlannedTimes(PlannedTime plannedTime) {
        this.plannedTime = plannedTime;
    }

    public ActualTime getActualTimes() {
        return actualTime;
    }

    public void setActualTimes(ActualTime actualTime) {
        this.actualTime = actualTime;
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
