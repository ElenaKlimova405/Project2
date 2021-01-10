package com.tasktracker.tasks.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "tasks")
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long task_id;
    @NonNull
    private String task_name;
    private String task_preview;
    private String task_description;
    private Long views = 0L;
    @OneToOne(fetch = FetchType.EAGER)
    /*@JsonIgnore*/
    //@JoinColumn(name = "user_id")
    @JoinColumn(name = "parent_task_id")
    //@Column(name = "parent_task_id")
    private Tasks parent_task;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    //@NonNull
    //@JoinColumn(name = "user_id")
    //@Column(name = "author_id")
    /*@JsonIgnore*/
    private Users author;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_selected_the_task_id")
    /*@JsonIgnore*/
    //@JoinColumn(name = "user_id")
    //@Column(name = "user_selected_the_task_id")
    private Users user_selected_the_task;
    private String taking_time;
    private String completion_time;
    private String busy_time;

    public Tasks() {
    }

    public Tasks(String task_name, String task_preview, String task_description, /*Long views, */Tasks parent_task, Users author, Users user_selected_the_task, String taking_time, String completion_time, String busy_time) {
        this.task_name = task_name;
        this.task_preview = task_preview;
        this.task_description = task_description;
        //this.views = views;
        this.parent_task = parent_task;
        this.author = author;
        this.user_selected_the_task = user_selected_the_task;
        this.taking_time = taking_time;
        this.completion_time = completion_time;
        this.busy_time = busy_time;
    }

    public void incrementViews() {
        this.views++;
    }

    public Long getTask_id() {
        return task_id;
    }

    public void setTask_id(Long task_id) {
        this.task_id = task_id;
    }

    public String getTask_name() {
        return task_name != null ? task_name : "<none>";
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getTask_preview() {
        return task_preview != null ? task_preview : "<none>";
    }

    public void setTask_preview(String task_preview) {
        this.task_preview = task_preview;
    }

    public String getTask_description() {
        return task_description != null ? task_description : "<none>";
    }

    public void setTask_description(String task_description) {
        this.task_description = task_description;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Tasks getParent_task() {
        return parent_task;
    }

    public void setParent_task(Tasks parent_task) {
        this.parent_task = parent_task;
    }

    public Users getAuthor() {
        return author;
    }

    public void setAuthor(Users author) {
        this.author = author;
    }

    public Users getUser_selected_the_task() {
        return user_selected_the_task;
    }

    public void setUser_selected_the_task(Users user_selected_the_task) {
        this.user_selected_the_task = user_selected_the_task;
    }

    public String getTaking_time() {
        return taking_time != null ? taking_time : "<none>";
    }

    public void setTaking_time(String taking_time) {
        this.taking_time = taking_time;
    }

    public String getCompletion_time() {
        return completion_time != null ? completion_time : "<none>";
    }

    public void setCompletion_time(String completion_time) {
        this.completion_time = completion_time;
    }

    public String getBusy_time() {
        return busy_time != null ? busy_time : "<none>";
    }

    public void setBusy_time(String busy_time) {
        this.busy_time = busy_time;
    }
}
