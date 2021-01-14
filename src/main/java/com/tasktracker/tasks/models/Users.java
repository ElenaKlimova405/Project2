package com.tasktracker.tasks.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class Users implements UserDetails {
//    @Id
//    @SequenceGenerator(name = "userIdSequenceGen",
//            sequenceName="userIdSequence", initialValue = 2)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userIdSequenceGen")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    //@NonNull
    @Column(unique = true)
    private String username;
    //@NonNull
    private String password;
    private boolean active = true;
    @ElementCollection(targetClass = Roles.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "userRole", joinColumns = @JoinColumn(name = "userId"))
    @Enumerated(EnumType.STRING)
    private Set<Roles> roles;

    private String eMail;
    private String activationCode;

    //@NonNull
    private String firstName;
    private String lastName;
    private String secondName;
    private String aboutMe;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    //@JsonIgnore
    @Column(name = "createdTaskId")
    private List<Tasks> createdTasks;
    @OneToMany(mappedBy = "userSelectedTheTask"/*, cascade = CascadeType.ALL, orphanRemoval = true*/)
    //@JsonIgnore
    @Column(name = "selectedTaskId")
    private List<Tasks> selectedTasks;

    public Users() {
    }


    public Users(String username, String password, boolean active, Set<Roles> roles, String eMail, String firstName, String lastName, String secondName, String aboutMe, List<Tasks> createdTasks, List<Tasks> selectedTasks) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.roles = roles;
        this.eMail = eMail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.secondName = secondName;
        this.aboutMe = aboutMe;
        this.createdTasks = createdTasks;
        this.selectedTasks = selectedTasks;
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

    public Set<Roles> getRoles() {
        return roles;
    }

    public void setRoles(Set<Roles> roles) {
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

    public List<Tasks> getCreatedTasks() {
        return createdTasks;
    }

    public void setCreatedTasks(List<Tasks> createdTasks) {
        this.createdTasks = createdTasks;
    }

    public List<Tasks> getSelectedTasks() {
        return selectedTasks;
    }

    public void setSelectedTasks(List<Tasks> selectedTasks) {
        this.selectedTasks = selectedTasks;
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

    public boolean equalsdLoginAndPasswordAndRole(Users user) {
        if (
                user != null &&
                this.getUsername().equals(user.getUsername()) &&
                this.getPassword().equals(user.getPassword())
        ) {
            for (Roles role : this.getRoles()) {
                if (
                        user.getRoles().containsAll(this.getRoles())
                ) {
                    return true;
                }
            }
        }
        return false;
    }
}
