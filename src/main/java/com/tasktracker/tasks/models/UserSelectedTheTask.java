package com.tasktracker.tasks.models;

import javax.persistence.*;

@Entity
@Table(name = "userSelectedTheTask")
public class UserSelectedTheTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long usersSelectedTheTaskId;

    @OneToOne(mappedBy = "userSelectedTheTask")
    @JoinColumn(name = "taskId")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public UserSelectedTheTask() {
    }

    public UserSelectedTheTask(Task task, User user) {
        this.task = task;
        this.user = user;
    }

    public Long getUsersSelectedTheTaskId() {
        return usersSelectedTheTaskId;
    }

    public void setUsersSelectedTheTaskId(Long usersSelectedTheTaskId) {
        this.usersSelectedTheTaskId = usersSelectedTheTaskId;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
