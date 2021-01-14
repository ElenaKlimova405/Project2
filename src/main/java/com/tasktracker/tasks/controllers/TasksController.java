package com.tasktracker.tasks.controllers;

import com.tasktracker.tasks.models.Tasks;
import com.tasktracker.tasks.models.Timer;
import com.tasktracker.tasks.models.Users;
import com.tasktracker.tasks.repo.TasksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class TasksController {
    @Autowired
    public TasksRepository tasksRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/tasks")
    public String tasksMain(Model model) {
//        System.out.println(jdbcTemplate);
//        List<Map<String, Object>> maps = jdbcTemplate.queryForList("Select * from Tasks");
//        System.out.println(maps);

        //Iterable<Tasks> tasks = tasksRepository.findAll();
        List<Tasks> parentTasks = tasksRepository.findParents();
        //model.addAttribute("tasks", tasks);
        model.addAttribute("parentTasks", parentTasks);
        return "tasks-main";
    }

    @GetMapping("/tasks/add")
    public String tasksAdd(Model model) {
        return "tasks-add1";
    }

    @PostMapping("/tasks/add")
    public String tasksPostAdd(
            @AuthenticationPrincipal Users user,
            @RequestParam(required = true) String taskName,
            @RequestParam(required = false, defaultValue = "") String taskPreview,
            @RequestParam(required = false, defaultValue = "") String taskDescription,
            Model model
    ) {
        Tasks task = new Tasks(taskName,
                taskPreview,
                taskDescription,
                0L,
                null,
                user,
                null,
                new Timer(),
                new Timer()
        );
        tasksRepository.save(task); // сохранение нового объекта
        return "redirect:/tasks/" + task.getTaskId();
    }

    @GetMapping("/tasks/{id}")
    public String tasksDetails(@PathVariable(value = "id") long idParam, Model model) {
        if (!tasksRepository.existsById(idParam)) {
            return "redirect:/tasks";
        }
        Optional<Tasks> task = tasksRepository.findById(idParam);
        ArrayList<Tasks> currentTask = new ArrayList<>();
        task.ifPresent(currentTask::add);
        List<Tasks> children = null;
        for (Tasks oneTask : currentTask) {
            oneTask.incrementViews();
            tasksRepository.save(oneTask);
            children = tasksRepository.findChildren(oneTask);
        }
        model.addAttribute("currentTask", currentTask);
        model.addAttribute("children", children);
        return "tasks-details";
    }

    @GetMapping("/tasks/{id}/edit")
    public String tasksEdit(@PathVariable(value = "id") long idParam, Model model) {
        if (!tasksRepository.existsById(idParam)) {
            return "redirect:/tasks/{id}";
        }
        Optional<Tasks> task = tasksRepository.findById(idParam);
        List<Tasks> res = new ArrayList<>();
        task.ifPresent(res::add);
        model.addAttribute("task", res);
        return "tasks-edit";
    }

    @PostMapping("/tasks/{id}/edit")
    public String tasksPostUpdate(@PathVariable(value = "id") long id,
                                  @RequestParam(required = true) String taskName,
                                  @RequestParam(required = false, defaultValue = "") String taskPreview,
                                  @RequestParam(required = false, defaultValue = "") String taskDescription,
                                  Model model) {
        Tasks task = tasksRepository.findById(id).orElseThrow();
        task.setTaskName(taskName);
        task.setTaskPreview(taskPreview);
        task.setTaskDescription(taskDescription);
        tasksRepository.save(task);
        return "redirect:/tasks/" + task.getTaskId();
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
                                         @RequestParam(required = true) String taskName,
                                         @RequestParam(required = false, defaultValue = "") String taskPreview,
                                         @RequestParam(required = false, defaultValue = "") String taskDescription,
                                         Model model) {
        if (!tasksRepository.existsById(id)) {
            return "redirect:/tasks";
        }
        Tasks parent = tasksRepository.findById(id).orElseThrow();
        Tasks subtask = new Tasks(
                taskName,
                taskPreview,
                taskDescription,
                0L,
                parent,
                user,
                null,
                new Timer(),
                new Timer()
        );

        tasksRepository.save(subtask); // сохранение нового объекта
        return "redirect:/tasks/" + subtask.getTaskId();
    }


    @GetMapping("/tasks/{id}/get_task")
    public String tasksGetGetTask(@PathVariable(value = "id") long id,
                                  @AuthenticationPrincipal Users user,
                                  Model model) {
        Tasks task = tasksRepository.findById(id).orElseThrow();
        task.setUserSelectedTheTask(user);
        tasksRepository.save(task);
        return "redirect:/tasks/{id}";
    }


//    @GetMapping("/tasks/{id}/mark_solved")
//    public String tasksGetMarkSolved(@PathVariable(value = "id") long id,
//                                     @AuthenticationPrincipal Users user,
//                                     Model model) throws ParseException {
//        Tasks task = tasksRepository.findById(id).orElseThrow();
//        DateFormat simpleDateFormat = new SimpleDateFormat();
//        task.setActualTime(simpleDateFormat.format(new Date()));
//
//        long difference = simpleDateFormat.parse(task.getActualTime()).getTime() - simpleDateFormat.parse(task.getTakingTime()).getTime();
//        String diffTime = "";
//        diffTime = (difference / (1000 * 60 * 60 * 24)) + " дней ";
//        difference %= (1000 * 60 * 60 * 24);
//        diffTime += difference / (1000 * 60 * 60) + " часов ";
//        difference %= (1000 * 60 * 60);
//        diffTime += difference / (1000 * 60) + " минут";
//
//        task.setBusyTime(diffTime);
//        tasksRepository.save(task);
//        return "redirect:/tasks/{id}";
//    }


    @GetMapping("/tasks/{id}/get_task_cancel")
    public String tasksGetGetTaskCancel(@PathVariable(value = "id") long id,
                                        //@AuthenticationPrincipal Users user,
                                        Model model) {
        Tasks task = tasksRepository.findById(id).orElseThrow();
        task.setUserSelectedTheTask(null);
        tasksRepository.save(task);
        return "redirect:/tasks/{id}";
    }




    @GetMapping("/tasks/{id}/set_planned_time")
    public String tasksGetSetPlannedTime(@PathVariable(value = "id") long id,
                                        Model model) {
        return "tasks-set-planned-time";
    }

    @PostMapping("/tasks/{id}/set_planned_time")
    public String tasksPostSetPlannedTime(@PathVariable(value = "id") long id,
                                         @RequestParam(required = false, defaultValue = "0") int days,
                                         @RequestParam(required = false, defaultValue = "0") int hours,
                                         @RequestParam(required = false, defaultValue = "0") int minutes,
                                         Model model) {
        if (!tasksRepository.existsById(id)) {
            return "redirect:/tasks";
        }
        Tasks task = tasksRepository.findById(id).orElseThrow();
        Timer timer = new Timer(days, hours, minutes, task);
        task.setPlannedTime(timer);

        tasksRepository.save(task); // сохранение нового объекта
        return "redirect:/tasks/" + task.getTaskId();
    }


    @GetMapping("/tasks/{id}/change_actual_time")
    public String tasksGetChangeActualTime(@PathVariable(value = "id") long id,
                                         Model model) {
        return "tasks-change-actual-time";
    }

    @PostMapping("/tasks/{id}/change_actual_time")
    public String tasksPostChangeActualTime(@PathVariable(value = "id") long id,
                                          @RequestParam(required = false, defaultValue = "0") int days,
                                          @RequestParam(required = false, defaultValue = "0") int hours,
                                          @RequestParam(required = false, defaultValue = "0") int minutes,
                                          Model model) {
        if (!tasksRepository.existsById(id)) {
            return "redirect:/tasks";
        }
        Tasks task = tasksRepository.findById(id).orElseThrow();
        Timer timer = new Timer(days, hours, minutes, task);
        task.setActualTime(timer);

        tasksRepository.save(task); // сохранение нового объекта
        return "redirect:/tasks/" + task.getTaskId();
    }



    @GetMapping("/tasks/{id}/add_actual_time")
    public String tasksGetAddActualTime(@PathVariable(value = "id") long id,
                                           Model model) {
        return "tasks-add-actual-time";
    }

    @PostMapping("/tasks/{id}/add_actual_time")
    public String tasksPostAddActualTime(@PathVariable(value = "id") long id,
                                            @RequestParam(required = false, defaultValue = "0") int hours,
                                            @RequestParam(required = false, defaultValue = "0") int minutes,
                                            Model model) {
        if (!tasksRepository.existsById(id)) {
            return "redirect:/tasks";
        }
        Tasks task = tasksRepository.findById(id).orElseThrow();
        task.getActualTime().addTime(hours, minutes);


        tasksRepository.save(task); // сохранение нового объекта
        return "redirect:/tasks/" + task.getTaskId();
    }


    @GetMapping("/tasks/{id}/statistics")
    public String tasksGetStatistics(@PathVariable(value = "id") long id,
                                        Model model) {
        return "tasks-statistics";
    }
}


