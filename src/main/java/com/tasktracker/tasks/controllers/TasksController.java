package com.tasktracker.tasks.controllers;

import com.tasktracker.tasks.models.Tasks;
import com.tasktracker.tasks.models.Users;
import com.tasktracker.tasks.repo.TasksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    public String tasksPostAdd(
            @AuthenticationPrincipal Users user,
            @RequestParam String task_name,
            @RequestParam String task_preview,
            @RequestParam String task_description,
            Model model
    ) {
        Tasks task = new Tasks(task_name,
                task_preview,
                task_description,
                null,
                user,//null,
                null,
                null,
                null,
                null
        );
        tasksRepository.save(task); // сохранение нового объекта
        return "redirect:/tasks/" + task.getTask_id();
    }

    @GetMapping("/tasks/{id}")
    public String tasksDetails(@PathVariable(value = "id") long idParam, Model model) {
        if (!tasksRepository.existsById(idParam)) {
            return "redirect:/tasks";
        }
        Optional<Tasks> task = tasksRepository.findById(idParam);
        ArrayList<Tasks> current_task = new ArrayList<>();
        task.ifPresent(current_task::add);
        List<Tasks> children = null;
        for (Tasks oneTask : current_task) {
            oneTask.incrementViews();
            tasksRepository.save(oneTask);
            children = tasksRepository.findChildren(oneTask);
        }
        model.addAttribute("current_task", current_task);
        model.addAttribute("children", children);
        return "tasks-details";
    }

    @GetMapping("/tasks/{id}/edit")
    public String tasksEdit(@PathVariable(value = "id") long idParam, Model model) {
        if (!tasksRepository.existsById(idParam)) {
            return "redirect:/tasks/{id}";
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

    @GetMapping("/tasks/{id}/remove")
    public String tasksPostDelete(@PathVariable(value = "id") long id, Model model) {
        Tasks task = tasksRepository.findById(id).orElseThrow();
        tasksRepository.delete(task);
        return "redirect:/tasks";
    }

    @GetMapping("/tasks/{id}/create_subtask")
    public String tasksGetCreateSubtask(@PathVariable(value = "id") long id, Model model) {
        Tasks parent = tasksRepository.findById(id).orElseThrow();
        model.addAttribute("parent", parent);
        return "tasks-create-subtask";
    }

    @PostMapping("/tasks/{id}/create_subtask")
    public String tasksPostCreateSubtask(@PathVariable(value = "id") long id,
                                         @AuthenticationPrincipal Users user,
                                         @RequestParam String task_name,
                                         @RequestParam String task_preview,
                                         @RequestParam String task_description,
                                         Model model) {
        if (!tasksRepository.existsById(id)) {
            return "redirect:/tasks";
        }
        Tasks parent = tasksRepository.findById(id).orElseThrow();
        Tasks subtask = new Tasks(task_name,
                task_preview,
                task_description,
                parent,
                user,
                null,
                null,
                null,
                null
        );
        tasksRepository.save(subtask); // сохранение нового объекта
        return "redirect:/tasks/" + subtask.getTask_id();
    }


    @GetMapping("/tasks/{id}/get_task")
    public String tasksGetGetTask(@PathVariable(value = "id") long id,
                                  @AuthenticationPrincipal Users user,
                                  Model model) {
        Tasks task = tasksRepository.findById(id).orElseThrow();
        task.setUser_selected_the_task(user);
        task.setTaking_time(new SimpleDateFormat().format(new Date()));
        tasksRepository.save(task);
        return "redirect:/tasks/{id}";
    }


    @GetMapping("/tasks/{id}/mark_solved")
    public String tasksGetMarkSolved(@PathVariable(value = "id") long id,
                                     @AuthenticationPrincipal Users user,
                                     Model model) throws ParseException {
        Tasks task = tasksRepository.findById(id).orElseThrow();
        DateFormat simpleDateFormat = new SimpleDateFormat();
        task.setCompletion_time(simpleDateFormat.format(new Date()));

        long difference = simpleDateFormat.parse(task.getCompletion_time()).getTime() - simpleDateFormat.parse(task.getTaking_time()).getTime();
        String diff_time = "";
        diff_time = (difference / (1000 * 60 * 60 * 24)) + " дней ";
        difference %= (1000 * 60 * 60 * 24);
        diff_time += difference / (1000 * 60 * 60) + " часов ";
        difference %= (1000 * 60 * 60);
        diff_time += difference / (1000 * 60) + " минут";

        task.setBusy_time(diff_time);
        tasksRepository.save(task);
        return "redirect:/tasks/{id}";
    }


    @GetMapping("/tasks/{id}/get_task_cancel")
    public String tasksGetGetTaskCancel(@PathVariable(value = "id") long id,
                                        //@AuthenticationPrincipal Users user,
                                        Model model) {
        Tasks task = tasksRepository.findById(id).orElseThrow();
        task.setUser_selected_the_task(null);
        task.setTaking_time(null);
        task.setCompletion_time(null);
        task.setBusy_time(null);
        tasksRepository.save(task);
        return "redirect:/tasks/{id}";
    }
}


