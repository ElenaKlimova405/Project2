package com.tasktracker.tasks.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long user_id;
    //@NonNull
    @Column(unique = true)
    private String username;
    //@NonNull
    private String password;
    private boolean active = true;
    @ElementCollection(targetClass = Roles.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Roles> roles;

    private String e_mail;
    private String activationCode;

    //@NonNull
    private String first_name;
    private String last_name;
    private String second_name;
    private String about_me;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL/*, orphanRemoval = true*/)
    //@JsonIgnore
    @Column(name = "created_task_id")
    private List<Tasks> created_tasks;
    @OneToMany(mappedBy = "user_selected_the_task", cascade = CascadeType.ALL/*, orphanRemoval = true*/)
    //@JsonIgnore
    @Column(name = "selected_task_id")
    private List<Tasks> selected_tasks;

    public Users() {
    }


    public Users(String username, String password, boolean active, Set<Roles> roles, String e_mail, String first_name, String last_name, String second_name, String about_me, List<Tasks> created_tasks, List<Tasks> selected_tasks) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.roles = roles;
        this.e_mail = e_mail;
        this.first_name = first_name;
        this.last_name = last_name;
        this.second_name = second_name;
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

    @Override
    public String getUsername() {
        return username != null ? username : "";
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password != null ? password : "";
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Roles> getRoles() {
        return roles;
    }

    public void setRoles(Set<Roles> roles) {
        this.roles = roles;
    }

    public String getE_mail() {
        return e_mail != null ? e_mail : "";
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getFirst_name() {
        return first_name != null ? first_name : "";
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name != null ? last_name : "";
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getSecond_name() {
        return second_name != null ? second_name : "";
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public String getAbout_me() {
        return about_me != null ? about_me : "";
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isActive();
    }

}
