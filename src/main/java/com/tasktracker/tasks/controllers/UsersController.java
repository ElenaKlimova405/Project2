package com.tasktracker.tasks.controllers;

import com.tasktracker.tasks.models.*;
import com.tasktracker.tasks.repo.TaskRepository;
import com.tasktracker.tasks.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.persistence.EntityManager;
import java.util.Iterator;

@Controller
public class UsersController {
    @Autowired
    public UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EntityManager entityManager;

    @GetMapping("/users")
    public String usersMain(Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users-main";
    }

    @GetMapping("/users/add")
    public String tasksAdd(Model model) {
        return "users-add";
    }

    @GetMapping("/users/{id}")
    public String usersDetails(@AuthenticationPrincipal User userIn,
                               @PathVariable(value = "id") long id,
                               Model model) {
        if (!userRepository.existsById(id)) {
            return "redirect:/users";
        }

        User user = userRepository.findById(id).orElseThrow();
        boolean iAmCurrentUser = false;
        boolean iAmAdministrator = false;

        if (userIn.getUsername().equals(user.getUsername())) {
            iAmCurrentUser = true;
        }

        if (userIn.hasRoleAdministrator()) {
            iAmAdministrator = true;
        }

        if (iAmCurrentUser || iAmAdministrator) {
            model.addAttribute("editButton", "true");
        }

        if (!iAmCurrentUser && iAmAdministrator) {
            model.addAttribute("deleteButton", "true");
        }

        model.addAttribute("currentUser", user);
        return "users-details";
    }

    @GetMapping("/users/my_account")
    public String usersGetMyAccount(@AuthenticationPrincipal User user, Model model) {
        return "redirect:/users/" + user.getUserId();
    }

    @GetMapping("/users/{id}/edit")
    public String usersEdit(@AuthenticationPrincipal User userIn,
                            @PathVariable(value = "id") long id,
                            Model model) {
        if (!userRepository.existsById(id)) {
            return "redirect:/users";
        }

        User user = userRepository.findById(id).orElseThrow();
        boolean iAmCurrentUser = false;
        boolean iAmAdministrator = false;

        if (userIn.getUsername().equals(user.getUsername())) {
            iAmCurrentUser = true;
        }

        if (userIn.hasRoleAdministrator()) {
            iAmAdministrator = true;
        }

        if (iAmAdministrator) {
            model.addAttribute("iAmAdministrator", "true");
        }
        if (iAmCurrentUser || iAmAdministrator) {
            model.addAttribute("iCanEdit", "true");
        }

        model.addAttribute("user", user);
        return "users-edit";
    }

    @PostMapping("/users/{id}/edit")
    public String usersPostUpdate(@PathVariable(value = "id") long id,
                                  @RequestParam(required = false, defaultValue = "") String firstName,
                                  @RequestParam(required = false, defaultValue = "") String lastName,
                                  @RequestParam(required = false, defaultValue = "") String secondName,
                                  @RequestParam(required = false, defaultValue = "") String eMail,
                                  @RequestParam(required = false, defaultValue = "") String aboutMe,

                                  @RequestParam(required = false) String programmerCheckBox,
                                  @RequestParam(required = false) String administratorCheckBox,

                                  @RequestParam(required = false) String oldPassword,
                                  @RequestParam(required = false) String newPassword,
                                  @RequestParam(required = false) String newPassword2,

                                  @AuthenticationPrincipal User userIn,
                                  Model model) {
        if (!userRepository.existsById(id)) {
            return "redirect:/users";
        }

        User user = userRepository.findById(id).orElseThrow();
        boolean iAmCurrentUser = false;
        boolean iAmAdministrator = false;
        if (userIn.getUsername().equals(user.getUsername())) {
            iAmCurrentUser = true;
        }

        if (userIn.hasRoleAdministrator()) {
            iAmAdministrator = true;
        }

        if (iAmCurrentUser || iAmAdministrator) {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setSecondName(secondName);
            user.setEMail(eMail);
            user.setAboutMe(aboutMe);

            if (!user.hasRoleUser()) {
                user.getRoles().add(Role.USER);
            }

            if (programmerCheckBox != null) {
                if (!user.hasRoleProgrammer()) {
                    user.getRoles().add(Role.PROGRAMMER);
                }
            } else {
                user.getRoles().remove(Role.PROGRAMMER);
            }

            if (administratorCheckBox != null) {
                if (!user.hasRoleAdministrator()) {
                    user.getRoles().add(Role.ADMINISTRATOR);
                }
            } else {
                user.getRoles().remove(Role.ADMINISTRATOR);
            }

            String encodedPassword = passwordEncoder.encode(oldPassword);
            String hasPassword = user.getPassword();
            boolean matches = passwordEncoder.matches(oldPassword, hasPassword);
            if (!newPassword.isEmpty() &&
                    !newPassword2.isEmpty() &&
                    newPassword.equals(newPassword2) &&
                    matches
            ) {
                user.setPassword(passwordEncoder.encode(newPassword));
            } else {
                if (!(newPassword.isEmpty() && newPassword2.isEmpty())) {
                    iAmCurrentUser = false;
                    iAmAdministrator = false;

                    if (userIn.getUsername().equals(user.getUsername())) {
                        iAmCurrentUser = true;
                    }

                    if (userIn.hasRoleAdministrator()) {
                        iAmAdministrator = true;
                    }

                    if (iAmAdministrator) {
                        model.addAttribute("iAmAdministrator", "true");
                    }
                    if (iAmCurrentUser || iAmAdministrator) {
                        model.addAttribute("iCanEdit", "true");
                    }

                    model.addAttribute("user", user);
                    model.addAttribute("ATTENTION", "Пароли введены неверно");

                    return "users-edit";
                }
            }

            userRepository.save(user);
        }
        return "redirect:/users/" + user.getUserId();
    }

    @PostMapping("/users/{id}/remove")
    public String usersGetRemove(@PathVariable(value = "id") long id,
                                 @AuthenticationPrincipal User userIn,
                                 Model model) {
        if (!userRepository.existsById(id)) {
            return "redirect:/users";
        }

        User user = userRepository.findById(id).orElseThrow();
        boolean iAmCurrentUser = false;
        boolean iAmAdministrator = false;

        if (userIn.getUsername().equals(user.getUsername())) {
            iAmCurrentUser = true;
        }

        if (userIn.hasRoleAdministrator()) {
            iAmAdministrator = true;
        }

        if (!iAmCurrentUser && iAmAdministrator) {
            if (user.getAuthor() != null) {
                Iterator<Author> iterator = user.getAuthor().iterator();
                while (iterator.hasNext()) {
                    Author currentAuthor = iterator.next();
                    Task byAuthor = taskRepository.findByAuthor(currentAuthor);
                    if (byAuthor != null) {
                        taskRepository.delete(byAuthor);
                    }
                }
            }

            if (user.getUserSelectedTheTask() != null) {
                Iterator<UserSelectedTheTask> iterator = user.getUserSelectedTheTask().iterator();
                while (iterator.hasNext()) {
                    UserSelectedTheTask currentUserSelectedTheTask = iterator.next();
                    Task byUserSelectedTheTask = taskRepository.findByUserSelectedTheTask(currentUserSelectedTheTask);
                    if (byUserSelectedTheTask != null) {
                        taskRepository.delete(byUserSelectedTheTask);
                    }
                }
            }

            entityManager.detach(user);
            user = userRepository.findById(id).orElseThrow();
            userRepository.delete(user);
        }
        return "redirect:/users";
    }
}
