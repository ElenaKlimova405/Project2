package com.tasktracker.tasks.controllers;

import com.tasktracker.tasks.models.Tasks;
import com.tasktracker.tasks.repo.TasksRepository;
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
public class TasksController {
    @Autowired
    public TasksRepository tasksRepository;

    @GetMapping("/tasks")
    public String tasksMain(Model model) {
        Iterable<Tasks> tasks = tasksRepository.findAll();
        model.addAttribute("tasks", tasks);
        return "tasks-main";
    }

    @GetMapping("/tasks/add")
    public String tasksAdd(Model model) {
        return "tasks-add1";
    }

    @PostMapping("/tasks/add")
    public String tasksPostAdd(@RequestParam String task_name, @RequestParam String task_preview, @RequestParam String task_description, Model model) {
        Tasks task = new Tasks(task_name,
                task_preview,
                task_description,
                //0L,
                null,
                null,
                null,
                null,
                null,
                null
                );
        tasksRepository.save(task); // сохранение нового объекта
        return "redirect:/tasks";
    }

   @GetMapping("/tasks/{id}")
    public String tasksDetails(@PathVariable(value = "id") long idParam, Model model) {
        if (!tasksRepository.existsById(idParam)) {
            return "redirect:/tasks";
        }
        Optional<Tasks> task = tasksRepository.findById(idParam);
        ArrayList<Tasks> current_task = new ArrayList<>();
        task.ifPresent(current_task::add);
        for (Tasks oneTask : current_task) {
            oneTask.incrementViews();
            tasksRepository.save(oneTask);
        }
        model.addAttribute("current_task", current_task);
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
    public String tasksPostUpdate(@PathVariable(value = "id") long id, @RequestParam String task_name, @RequestParam String task_preview, @RequestParam String task_description, Model model) {
        Tasks task = tasksRepository.findById(id).orElseThrow();
        task.setTask_name(task_name);
        task.setTask_preview(task_preview);
        task.setTask_description(task_description);
        tasksRepository.save(task);
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/{id}/remove")
    public String tasksPostDelete(@PathVariable(value = "id") long id, Model model) {
        Tasks task = tasksRepository.findById(id).orElseThrow();
        tasksRepository.delete(task);
        return "redirect:/tasks";
    }
}
