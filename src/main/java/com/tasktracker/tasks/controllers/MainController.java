package com.tasktracker.tasks.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

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


}