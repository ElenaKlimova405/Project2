package com.tasktracker.tasks.controllers;

import com.tasktracker.tasks.models.Tasks;
import com.tasktracker.tasks.models.Users;
import com.tasktracker.tasks.repo.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
/*
    @GetMapping("/tasks/add")
    public String tasksAdd(Model model) {
        return "tasks-add";
    }

    @PostMapping("/tasks/add")
    public String tasksPostAdd(@RequestParam String task_name, @RequestParam String parent_task_id, Model model) {
        Long parent_task_idAsLong;
        parent_task_idAsLong =  Long.parseLong(parent_task_id);
        Tasks task = new Tasks(task_name, parent_task_idAsLong);
        tasksRepository.save(task); // сохранение нового объекта
        return "redirect:/tasks";
    }

    @GetMapping("/tasks/{id}")
    public String tasksDetails(@PathVariable(value = "id") long idParam, Model model) {
        if (!tasksRepository.existsById(idParam)) {
            return "redirect:/tasks";
        }
        Optional<Tasks> task = tasksRepository.findById(idParam);
        ArrayList<Tasks> res = new ArrayList<>();
        task.ifPresent(res::add);
        model.addAttribute("task", res);
        return "tasks-details";
    }

    @GetMapping("/tasks/{id}/edit")
    public String tasksEdit(@PathVariable(value = "id") long idParam, Model model) {
        if (!tasksRepository.existsById(idParam)) {
            return "redirect:/tasks";
        }
        Optional<Tasks> task = tasksRepository.findById(idParam);
        ArrayList<Tasks> res = new ArrayList<>();
        task.ifPresent(res::add);
        model.addAttribute("task", res);
        return "tasks-edit";
    }

    @PostMapping("/tasks/{id}/edit")
    public String tasksPostUpdate(@PathVariable(value = "id") long id, @RequestParam String task_name, @RequestParam String parent_task_id, Model model) {
        Tasks task = tasksRepository.findById(id).orElseThrow();
        task.setTask_name(task_name);
        task.setParent_task_id(Long.parseLong(parent_task_id));
        tasksRepository.save(task);
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/{id}/remove")
    public String tasksPostDelete(@PathVariable(value = "id") long id, Model model) {
        Tasks task = tasksRepository.findById(id).orElseThrow();
        tasksRepository.delete(task);
        return "redirect:/tasks";
    }
*/
}
