package com.tasktracker.tasks.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "usr")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(unique = true)
    private String username;
    private String password;
    private boolean active = true;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "userRole", joinColumns = @JoinColumn(name = "userId"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    private String eMail;
    private String activationCode;
    private String firstName;
    private String lastName;
    private String secondName;
    private String aboutMe;


    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @Column(name = "authorId")
    private List<Author> author;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @Column(name = "userSelectedTheTaskId")
    private List<UserSelectedTheTask> userSelectedTheTask;

    public User() {
    }

    public User(String username, String password, boolean active, Set<Role> roles, String eMail, String activationCode, String firstName, String lastName, String secondName, String aboutMe, List<Author> author, List<UserSelectedTheTask> userSelectedTheTask) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.roles = roles;
        this.eMail = eMail;
        this.activationCode = activationCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.secondName = secondName;
        this.aboutMe = aboutMe;
        this.author = author;
        this.userSelectedTheTask = userSelectedTheTask;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getEMail() {
        return eMail != null ? eMail : "";
    }

    public void setEMail(String eMail) {
        this.eMail = eMail;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getFirstName() {
        return firstName != null ? firstName : "";
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName != null ? lastName : "";
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSecondName() {
        return secondName != null ? secondName : "";
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getAboutMe() {
        return aboutMe != null ? aboutMe : "";
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public List<Author> getAuthor() {
        return author;
    }

    public void setAuthor(List<Author> author) {
        this.author = author;
    }

    public List<UserSelectedTheTask> getUserSelectedTheTask() {
        return userSelectedTheTask;
    }

    public void setUserSelectedTheTask(List<UserSelectedTheTask> userSelectedTheTask) {
        this.userSelectedTheTask = userSelectedTheTask;
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

    public boolean equalsdLoginAndPasswordAndRole(User user) {
        if (user != null &&
                this.getUsername().equals(user.getUsername()) &&
                this.getPassword().equals(user.getPassword())) {
            for (Role role : this.getRoles()) {
                if (user.getRoles().containsAll(this.getRoles())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasRole(Role role) {
        for (Role role1 : this.getRoles()) {
            if (role1.equals(role))
                return true;
        }
        return false;
    }

    public boolean hasRoleUser() {
        for (Role role1 : this.getRoles()) {
            if (role1.equals(Role.USER))
                return true;
        }
        return false;
    }

    public boolean hasRoleAdministrator() {
        for (Role role1 : this.getRoles()) {
            if (role1.equals(Role.ADMINISTRATOR))
                return true;
        }
        return false;
    }

    public boolean hasRoleProgrammer() {
        for (Role role1 : this.getRoles()) {
            if (role1.equals(Role.PROGRAMMER))
                return true;
        }
        return false;
    }
}
