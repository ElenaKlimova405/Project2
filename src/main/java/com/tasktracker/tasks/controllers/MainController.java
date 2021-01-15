package com.tasktracker.tasks.controllers;

import com.tasktracker.tasks.models.Roles;
import com.tasktracker.tasks.models.Users;
import com.tasktracker.tasks.repo.UsersRepository;
import com.tasktracker.tasks.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
//import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    private final UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsersService usersService;


    @Autowired
    public MainController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    @GetMapping("/")
    public String home(/*@RequestParam(name="name", required=false, defaultValue="World") String name, */Model model) {
        model.addAttribute("someAttribute", "Сюда можно вставить имя пользователя");
        return "home";
    }

    @GetMapping("/about")
    public String about(/*@RequestParam(name="name", required=false, defaultValue="World") String name, */Model model) {
        model.addAttribute("someAttribute", "Сюда можно вставить имя пользователя");
        return "about";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("someAttribute", "Сюда можно вставить имя пользователя");
//        usersService.checkAdminAccount();
        return "login";
    }

    /* @PostMapping("@{/login}")
     public String loginPost(@RequestParam(required=true) String username, @RequestParam(required=true) String password, Model model) {
         if (usersRepository.findByUsername(username) == null) {
             model.addAttribute("param.error", "Неверный логин или пароль!");
             return "login";
         }
         Users user = usersRepository.findByUsername(username);
         if (!password.equals(user.getPassword())) {
             model.addAttribute("param.error", "Неверный логин или пароль!");
             return "login";
         }

         user.setActive(true);
         model.addAttribute("userLogIn", user);

         return "redirect:/";
     }
 */
    @GetMapping("/registration")
    public String registration(/*@RequestParam(name="name", required=false, defaultValue="World") String name, */Model model) {
        model.addAttribute("someAttribute", "Сюда можно вставить имя пользователя");
        return "registration";
    }

    @PostMapping("/registration")
    public String AddUser(/*@RequestParam(name="name", required=false, defaultValue="World") String name,*/
            @RequestParam(required = true) String username,
            @RequestParam(required = true) String password,
            @RequestParam(required = true) String password2,
            Model model
    ) {
        Users userFromDb = usersRepository.findByUsername(username);

        if (userFromDb != null) {
            model.addAttribute("message", "Данный логин уже занят");
            return "registration";
        }

        if (!password.equals(password2)) {
            model.addAttribute("message", "Пароли не совпадают");
            return "registration";
        }

        Users user = new Users();
        user.setUsername(username);
        user.setActive(true);
        user.setRoles(Collections.singleton(Roles.USER));
        user.setPassword(passwordEncoder.encode(password));
        usersRepository.save(user);

        return "redirect:/login";
    }

}