package com.tasktracker.tasks.controllers;

import com.tasktracker.tasks.models.*;
import com.tasktracker.tasks.repo.TaskRepository;
import com.tasktracker.tasks.repo.UserRepository;
import com.tasktracker.tasks.service.MailSender;
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
    private MailSender mailSender;

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

//    // добавление пользователя, но без сообщений о подтверждении
//    @PostMapping("/users/add")
//    public String usersPostAdd(@RequestParam(required = true) String username,
//                               @RequestParam(required = true) String password,
//                               @RequestParam(required = true) String password2,
////                               @RequestParam(required = false) String first_name,
////                               @RequestParam(required = false) String last_name,
////                               @RequestParam(required = false) String second_name,
////                               @RequestParam(required = false) String e_mail,
////                               @RequestParam(required = false) String about_me,
//                               Model model) {
//        if (usersRepository.findByUsername(username) != null) {
//            model.addAttribute("message", "Данный логин уже занят");
//            return "users-add";
//        }
//
//        if (!password.equals(password2)) {
//            model.addAttribute("message", "Пароли не совпадают");
//            return "users-add";
//        }
//        Users users = new Users(username, password, true, null, null, null, null, null, null, null, null);
//        usersRepository.save(users); // сохранение нового объекта
//        return "redirect:/users/" + users.getUserId();
//    }

    @GetMapping("/users/{id}")
    public String usersDetails(@AuthenticationPrincipal User userIn,
                               @PathVariable(value = "id") long idParam,
                               Model model) {

        if (!userRepository.existsById(idParam)) {
            return "redirect:/users";
        }

        //HttpSession session = httpServletRequest.getSession();
        //long user_id = (long)session.getAttribute("user_id");

//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String userLogin=null;
//        if (principal instanceof UserDetails) {
//            UserDetails userDetails = (UserDetails) principal;
//            userLogin = userDetails.getUsername();
//            System.out.println(userLogin);
//        }


        User user = userRepository.findById(idParam).orElseThrow();
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


//        boolean accessTrue = user.get().getUsername().equals(userLogin);


        model.addAttribute("currentUser", user);
        return "users-details";
    }


    @GetMapping("/users/my_account")
    public String usersGetMyAccount(@AuthenticationPrincipal User user, Model model) {
        return "redirect:/users/" + user.getUserId();
    }


    @GetMapping("/users/{id}/edit")
    public String usersEdit(@AuthenticationPrincipal User userIn,
                            @PathVariable(value = "id") long idParam,
                            Model model) {
        if (!userRepository.existsById(idParam)) {
            return "redirect:/users";
        }

        User user = userRepository.findById(idParam).orElseThrow();
        boolean iAmCurrentUser = false;
        boolean iAmAdministrator = false;

        if (userIn.getUsername().equals(user.getUsername())) {
            iAmCurrentUser = true;
        }

        if (userIn.hasRoleAdministrator()) {
            iAmAdministrator = true;
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

                                  //@RequestParam(required = false) String userCheckBox,
                                  @RequestParam(required = false) String programmerCheckBox,
                                  @RequestParam(required = false) String administratorCheckBox,

                                  @RequestParam(required = false) String oldPassword,
                                  @RequestParam(required = false) String newPassword,
                                  @RequestParam(required = false) String newPassword2,

                                  @AuthenticationPrincipal User userIn,
                                  Model model) {
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


        /*if (!user.getEMail().equals(eMail)) {
            user.setActivationCode(UUID.randomUUID().toString());
        }*/

            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setSecondName(secondName);
            user.setEMail(eMail);
            user.setAboutMe(aboutMe);


            //if (userCheckBox != null) {
            if (!user.hasRoleUser()) {
                user.getRoles().add(Role.USER);
            }
//        } else {
//            user.getRoles().remove(Roles.USER);
//        }

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


            if (!(oldPassword != null && newPassword != null && newPassword2 != null) &&
                    !(oldPassword == null && newPassword == null && newPassword2 == null)) {
                model.addAttribute("ATTENTION", "Пароли введены неверно");
            } else {
                String encodedPassword = passwordEncoder.encode(oldPassword);
                if (newPassword.equals(newPassword2) && encodedPassword.equals(user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(newPassword));
                }
            }


            userRepository.save(user);

        /*if (!user.getEMail().isEmpty() && user.getEMail() != null) {
            String message = String.format(
                "Hello, %s! \n" +
                        "Welcome to Task tracker! Please, visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()

            );
            mailSender.send(user.getEMail(), "Activation code", message);
        }*/

        }
        return "redirect:/users/" + user.getUserId();
    }

    @PostMapping("/users/{id}/remove")
    public String usersGetRemove(@PathVariable(value = "id") long id,
                                 @AuthenticationPrincipal User userIn,
                                 Model model) {
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
                    //userRepository.deleteByAuthor(currentAuthor);
                    //taskRepository.deleteByAuthor(currentAuthor);
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
                    //userRepository.deleteByAuthor(currentAuthor);
                    //taskRepository.deleteByAuthor(currentAuthor);
                    Task byUserSelectedTheTask = taskRepository.findByUserSelectedTheTask(currentUserSelectedTheTask);
                    if (byUserSelectedTheTask != null) {
                        taskRepository.delete(byUserSelectedTheTask);
                    }
                }
            }

            //entityManager.merge(user);
//            entityManager.refresh(user);

//            entityManager
//                    .remove(entityManager.contains(user) ? user : entityManager.merge(user));
//            entityManager
//                    .remove(entityManager.getReference(User.class, user));



           userRepository.delete(user);

        }

        return "redirect:/users";
    }

//    @GetMapping("/users/{id}/activate")
//    public String usersGetActivate(@PathVariable(value = "id") long id, Model model) {
//        Users user = usersRepository.findById(id).orElseThrow();
//        user.setActive(true);
//        usersRepository.save(user);
//        return "redirect:/users";
//    }
//
//    @GetMapping("/users/{id}/deactivate")
//    public String usersGetDeactivate(@PathVariable(value = "id") long id, Model model) {
//        Users user = usersRepository.findById(id).orElseThrow();
//        user.setActive(false);
//        usersRepository.save(user);
//        return "redirect:/users";
//    }
}
