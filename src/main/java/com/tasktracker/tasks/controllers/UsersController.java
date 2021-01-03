package com.tasktracker.tasks.controllers;

import com.tasktracker.tasks.models.Users;
import com.tasktracker.tasks.repo.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class UsersController {
    @Autowired
    public UsersRepository usersRepository;

    @GetMapping("/users")
    public String usersMain(Model model) {
        Iterable<Users> users = usersRepository.findAll();
        model.addAttribute("users", users);
        return "users-main";
    }

    @GetMapping("/users/add")
    public String tasksAdd(Model model) {
        return "users-add";
    }

  @PostMapping("/users/add")
    public String usersPostAdd(@RequestParam String first_name, @RequestParam String last_name, @RequestParam String second_name, @RequestParam String e_mail, @RequestParam String about_me, Model model) {
        Users users = new Users(null, 0L, e_mail, first_name, last_name, second_name, about_me, null, null);
        usersRepository.save(users); // сохранение нового объекта
        return "redirect:/users";
    }

    @GetMapping("/users/{id}")
    public String usersDetails(@PathVariable(value = "id") long idParam, Model model) {
        if (!usersRepository.existsById(idParam)) {
            return "redirect:/users";
        }
        Optional<Users> user = usersRepository.findById(idParam);
        ArrayList<Users> current_user = new ArrayList<>();
        user.ifPresent(current_user::add);
        model.addAttribute("current_user", current_user);
        return "users-details";
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
    public String usersPostUpdate(@PathVariable(value = "id") long id, @RequestParam String first_name, @RequestParam String last_name, @RequestParam String second_name, @RequestParam String e_mail, @RequestParam String about_me, Model model) {
        Users user = usersRepository.findById(id).orElseThrow();
        user.setFirst_name(first_name);
        user.setLast_name(last_name);
        user.setSecond_name(second_name);
        user.setE_mail(e_mail);
        user.setAbout_me(about_me);
        usersRepository.save(user);
        return "redirect:/users";
    }

    @PostMapping("/users/{id}/remove")
    public String usersPostDelete(@PathVariable(value = "id") long id, Model model) {
        Users user = usersRepository.findById(id).orElseThrow();
        usersRepository.delete(user);
        return "redirect:/users";
    }
}
