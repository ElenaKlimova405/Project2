package com.tasktracker.tasks.controllers;

import com.tasktracker.tasks.models.Task;
import com.tasktracker.tasks.repo.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;
import java.util.Optional;

//@Controller
public class BlogController {

   /* @Autowired
    public TaskRepository taskRepository;

    @GetMapping("/blog")
    public String blogMain(Model model) {
        Iterable<Task> tasks = taskRepository.findAll();
        model.addAttribute("tasks", tasks);
        return "blog-main"; // если переходим на страницу "/blog", то выполняется шаблон "blog-main"
    }

    @GetMapping("/blog/add")
    public String blogAdd(Model model) {
        return "blog-add"; // если переходим на страницу "/blog/add", то выполняется шаблон "blog-add"
    }

    @PostMapping("/blog/add")
    public String blogPostAdd(@RequestParam String task_name, @RequestParam String parent_task_id, Model model) {
        Long parent_task_idAsLong;
        parent_task_idAsLong =  Long.parseLong(parent_task_id);
        Task task = new Task(task_name, parent_task_idAsLong);
        taskRepository.save(task); // сохранение нового объекта
        return "redirect:/blog"; // если передали данные на странице "/blog/add", то выполняется шаблон "blog-add"
    }

    @GetMapping("/blog/{id}")
    public String blogDetails(@PathVariable(value = "id") long idParam, Model model) {
        if (!taskRepository.existsById(idParam)) {
            return "redirect:/blog";
        }
        Optional<Task> task = taskRepository.findById(idParam);
        ArrayList<Task> res = new ArrayList<>();
        task.ifPresent(res::add);
        model.addAttribute("task", res);
        return "blog-details"; // выполняется шаблон "blog-add"
    }

    @GetMapping("/blog/{id}/edit")
    public String blogEdit(@PathVariable(value = "id") long idParam, Model model) {
        if (!taskRepository.existsById(idParam)) {
            return "redirect:/blog";
        }
        Optional<Task> task = taskRepository.findById(idParam);
        ArrayList<Task> res = new ArrayList<>();
        task.ifPresent(res::add);
        model.addAttribute("task", res);
        return "blog-edit"; // выполняется шаблон "blog-add"
    }

    @PostMapping("/blog/{id}/edit")
    public String blogPostUpdate(@PathVariable(value = "id") long id, @RequestParam String task_name, @RequestParam String parent_task_id, Model model) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setTask_name(task_name);
        task.setParent_task_id(Long.parseLong(parent_task_id));
        taskRepository.save(task); // обновление существующего объекта
        return "redirect:/blog"; //  выполняется шаблон "blog-add"
    }

    @PostMapping("/blog/{id}/remove")
    public String blogPostDelete(@PathVariable(value = "id") long id, Model model) {
        Task task = taskRepository.findById(id).orElseThrow();
        taskRepository.delete(task);
        return "redirect:/blog"; // выполняется шаблон "blog-add"
    }
*/
}
