package com.tasktracker.tasks.models;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;


public enum Roles implements GrantedAuthority {
    USER("Пользователь"),
    ADMINISTRATOR("Администратор"),
    PROGRAMMER("Программист");

    private String enumName;

    Roles(String enumName) {
        this.enumName = enumName;
    }

    public String getEnumName() {
        return enumName;
    }

    @Override
    public String getAuthority() {
        return name();
    }
}

/*
@Entity
public class Roles implements GrantedAuthority {

    @Id
    @SequenceGenerator(name = "role_id_sequence_gen",
            sequenceName="role_id_sequence", initialValue = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_id_sequence_gen")
    private Long role_id;
    private String name;

    @Column(unique = true)
    private String authority;

    public Long getId() {
        return role_id;
    }

    public void setId(Long role_id) {
        this.role_id = role_id;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return name;
    }
}

*/


/*
import org.springframework.security.core.GrantedAuthority;


public enum Roles implements GrantedAuthority {
    USER,
    ADMINISTRATOR;

    @Override
    public String getAuthority() {
        return name();
    }
}*/