package com.tasktracker.tasks.models;

import org.springframework.lang.NonNull;
//import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/*
@Entity
@Table(name = "roles")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long role_id;
    @NonNull
    private String role_name;
    //@OneToOne(fetch = FetchType.EAGER)
    //private Users user;

    public Long getRole_id() {
        return role_id;
    }

    public Roles() {
    }

    public Roles(String role_name) {
        this.role_name = role_name;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }
}
*/


/*
public enum Roles implements GrantedAuthority {
    USER,
    ADMINISTRATOR;


    @Override
    public String getAuthority() {
        return name();
    }
}*/