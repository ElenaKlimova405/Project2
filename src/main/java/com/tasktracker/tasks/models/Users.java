package com.tasktracker.tasks.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long user_id;
    @NonNull
    private String login;
    @NonNull
    private Long hashcode;
    private String e_mail;
    @NonNull
    private String first_name;
    private String last_name;
    private String second_name;
    @OneToOne(fetch = FetchType.EAGER)
    @NonNull
    @JsonIgnore
    private Roles role;
    private String about_me;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Tasks> created_tasks;
    @OneToMany(mappedBy = "user_selected_the_task", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Tasks> selected_tasks;

    public Users() {
    }

    public Users(@NonNull String login, @NonNull Long hashcode, String e_mail, @NonNull String first_name, String last_name, String second_name, @NonNull Roles role, String about_me, List<Tasks> created_tasks, List<Tasks> selected_tasks) {
        this.login = login;
        this.hashcode = hashcode;
        this.e_mail = e_mail;
        this.first_name = first_name;
        this.last_name = last_name;
        this.second_name = second_name;
        this.role = role;
        this.about_me = about_me;
        this.created_tasks = created_tasks;
        this.selected_tasks = selected_tasks;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    @NonNull
    public String getLogin() {
        return login;
    }

    public void setLogin(@NonNull String login) {
        this.login = login;
    }

    @NonNull
    public Long getHashcode() {
        return hashcode;
    }

    public void setHashcode(@NonNull Long hashcode) {
        this.hashcode = hashcode;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    @NonNull
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(@NonNull String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    @NonNull
    public Roles getRole() {
        return role;
    }

    public void setRole(@NonNull Roles role) {
        this.role = role;
    }

    public String getAbout_me() {
        return about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public List<Tasks> getCreated_tasks() {
        return created_tasks;
    }

    public void setCreated_tasks(List<Tasks> created_tasks) {
        this.created_tasks = created_tasks;
    }

    public List<Tasks> getSelected_tasks() {
        return selected_tasks;
    }

    public void setSelected_tasks(List<Tasks> selected_tasks) {
        this.selected_tasks = selected_tasks;
    }
}
