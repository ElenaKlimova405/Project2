package com.tasktracker.tasks.controllers;

import com.tasktracker.tasks.models.Roles;
import com.tasktracker.tasks.models.Users;
import com.tasktracker.tasks.repo.UsersRepository;
import com.tasktracker.tasks.service.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.management.relation.RoleList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Controller
public class UsersController {
    @Autowired
    public UsersRepository usersRepository;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public String usersMain(Model model) {
        Iterable<Users> users = usersRepository.findAll();
        model.addAttribute("users", users);
        return "users-main";
    }

//    @GetMapping("/users/add")
//    public String tasksAdd(Model model) {
//        return "users-add";
//    }
//
//    @PostMapping("/users/add")
//    public String usersPostAdd(@RequestParam(required = true) String username,
//                               @RequestParam(required = true) String password,
//                               @RequestParam(required = false) String first_name,
//                               @RequestParam(required = false) String last_name,
//                               @RequestParam(required = false) String second_name,
//                               @RequestParam(required = false) String e_mail,
//                               @RequestParam(required = false) String about_me,
//                               Model model) {
//        Users users = new Users(username, password, true, null, e_mail, first_name, last_name, second_name, about_me, null, null);
//        usersRepository.save(users); // сохранение нового объекта
//        return "redirect:/users/" + users.getUserId();
//    }

    @GetMapping("/users/{id}")
    public String usersDetails(@PathVariable(value = "id") long idParam,
                               Model model) {
        if (!usersRepository.existsById(idParam)) {
            return "redirect:/users";
        }
        //HttpSession session = httpServletRequest.getSession();
        //long user_id = (long)session.getAttribute("user_id");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String  userLogin=null;
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            userLogin = userDetails.getUsername();
            System.out.println(userLogin);
        }


        Optional<Users> user = usersRepository.findById(idParam);
        boolean accessTrue = user.get().getUsername().equals(userLogin);

        ArrayList<Users> currentUser = new ArrayList<>();
        user.ifPresent(currentUser::add);
        model.addAttribute("currentUser", currentUser);





        return "users-details";
    }


    @GetMapping("/users/my_account")
    public String usersGetMyAccount(@AuthenticationPrincipal Users user, Model model) {
        return "redirect:/users/" + user.getUserId();
    }

    @GetMapping("/users/{id}/edit")
    public String usersEdit(@PathVariable(value = "id") long idParam, Model model) {
        if (!usersRepository.existsById(idParam)) {
            return "redirect:/users";
        }
        Optional<Users> user = usersRepository.findById(idParam);
        ArrayList<Users> res = new ArrayList<>();
        user.ifPresent(res::add);
        model.addAttribute("user", res);
        return "users-edit";
    }

    @PostMapping("/users/{id}/edit")
    public String usersPostUpdate(@PathVariable(value = "id") long id,
                                  @RequestParam(required = false, defaultValue = "") String firstName,
                                  @RequestParam(required = false, defaultValue = "") String lastName,
                                  @RequestParam(required = false, defaultValue = "") String secondName,
                                  @RequestParam(required = false, defaultValue = "") String eMail,
                                  @RequestParam(required = false, defaultValue = "") String aboutMe,

                                  @RequestParam(required = false) String userCheckBox,
                                  @RequestParam(required = false) String programmerCheckBox,
                                  @RequestParam(required = false) String administratorCheckBox,

                                  Model model) {
        Users user = usersRepository.findById(id).orElseThrow();

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
                user.getRoles().add(Roles.USER);
            }
//        } else {
//            user.getRoles().remove(Roles.USER);
//        }

        if (programmerCheckBox != null) {
            if (!user.hasRoleProgrammer()) {
                user.getRoles().add(Roles.PROGRAMMER);
            }
        } else {
            user.getRoles().remove(Roles.PROGRAMMER);
        }

        if (administratorCheckBox != null) {
            if (!user.hasRoleAdministrator()) {
                user.getRoles().add(Roles.ADMINISTRATOR);
            }
        } else {
            user.getRoles().remove(Roles.ADMINISTRATOR);
        }


        usersRepository.save(user);

        /*if (!user.getEMail().isEmpty() && user.getEMail() != null) {
            String message = String.format(
                "Hello, %s! \n" +
                        "Welcome to Task tracker! Please, visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()

            );
            mailSender.send(user.getEMail(), "Activation code", message);
        }*/

        return "redirect:/users/" + user.getUserId();
    }

    @PostMapping("/users/{id}/remove")
    public String usersGetRemove(@PathVariable(value = "id") long id, Model model) {
        Users user = usersRepository.findById(id).orElseThrow();
        usersRepository.delete(user);
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/activate")
    public String usersGetActivate(@PathVariable(value = "id") long id, Model model) {
        Users user = usersRepository.findById(id).orElseThrow();
        user.setActive(true);
        usersRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/deactivate")
    public String usersGetDeactivate(@PathVariable(value = "id") long id, Model model) {
        Users user = usersRepository.findById(id).orElseThrow();
        user.setActive(false);
        usersRepository.save(user);
        return "redirect:/users";
    }
}
