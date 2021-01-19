package com.tasktracker.tasks.controllers;

import com.tasktracker.tasks.models.Role;
import com.tasktracker.tasks.models.User;
import com.tasktracker.tasks.repo.UserRepository;
import com.tasktracker.tasks.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Collections;

@Controller
public class MainController {
    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsersService usersService;
    @Autowired
    public MainController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("someAttribute", "Сюда можно вставить имя пользователя");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("someAttribute", "Сюда можно вставить имя пользователя");
        return "about";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("someAttribute", "Сюда можно вставить имя пользователя");
        return "login";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("someAttribute", "Сюда можно вставить имя пользователя");
        return "registration";
    }

    @PostMapping("/registration")
    public String AddUser(@RequestParam(required = true) String username,
                          @RequestParam(required = true) String password,
                          @RequestParam(required = true) String password2,
                          Model model
    ) {
        User userFromDb = userRepository.findByUsername(username);
        if (userFromDb != null) {
            model.addAttribute("message", "Данный логин уже занят");
            return "registration";
        }

        if (!password.equals(password2)) {
            model.addAttribute("message", "Пароли не совпадают");
            return "registration";
        }

        User user = new User();
        user.setUsername(username);
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return "redirect:/login";
    }
}