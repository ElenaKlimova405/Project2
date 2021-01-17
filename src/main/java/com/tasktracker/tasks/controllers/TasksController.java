package com.tasktracker.tasks.controllers;

import com.tasktracker.tasks.additional_classes.Triple;
import com.tasktracker.tasks.models.*;
import com.tasktracker.tasks.models.Timer;
import com.tasktracker.tasks.repo.TaskRepository;
import com.tasktracker.tasks.repo.UserSelectedTheTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
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
    public TaskRepository taskRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserSelectedTheTaskRepository userSelectedTheTaskRepository;

    @GetMapping("/tasks")
    public String tasksMain(Model model) {
//        System.out.println(jdbcTemplate);
//        List<Map<String, Object>> maps = jdbcTemplate.queryForList("Select * from Tasks");
//        System.out.println(maps);

        //Iterable<Tasks> tasks = tasksRepository.findAll();
        List<Task> parentTasks = taskRepository.findParents();
        //model.addAttribute("tasks", tasks);
        model.addAttribute("parentTasks", parentTasks);
        return "tasks-main";
    }

    @GetMapping("/tasks/add")
    public String tasksAdd(Model model) {
        return "tasks-add1";
    }

    @PostMapping("/tasks/add")
    public String tasksPostAdd(@AuthenticationPrincipal User userIn,
                               @RequestParam(required = true) String taskName,
                               @RequestParam(required = false, defaultValue = "") String taskPreview,
                               @RequestParam(required = false, defaultValue = "") String taskDescription,
                               Model model
    ) {

        PlannedTime plannedTime = new PlannedTime();
        plannedTime.setTimer(new Timer());
        ActualTime actualTime = new ActualTime();
        actualTime.setTimer(new Timer());
        Author author = new Author();
        author.setUser(userIn);
        List<Status> statuses = new ArrayList<>();
        statuses.add(Status.DISTRIBUTED);

        Task task = new Task(taskName,
                taskPreview,
                taskDescription,
                0L,
                null,
                null,
                author,
                null,
                plannedTime,
                actualTime,
                statuses
                );


        taskRepository.save(task); // сохранение нового объекта
        return "redirect:/tasks/" + task.getTaskId();
    }

    @GetMapping("/tasks/{id}")
    public String tasksDetails(@AuthenticationPrincipal User userIn,
                               @PathVariable(value = "id") long idParam,
                               Model model) {
        if (!taskRepository.existsById(idParam)) {
            return "redirect:/tasks";
        }

        Task task = taskRepository.findById(idParam).orElseThrow();
        List<Task> children = null;

        task.incrementViews();
        taskRepository.save(task);
        //taskRepository.findChildren(task);

        children = task.getChildren();
        model.addAttribute("currentTask", task);
        model.addAttribute("children", children);




        if (task.getUserSelectedTheTask() == null) {
            model.addAttribute("getTaskButton", "true");
        } else {
            if (userIn.getUsername().equals(task.getUserSelectedTheTask().getUser().getUsername())) {
                model.addAttribute("cancelTaskButton", "true");
            }
        }



        if ((task.getAuthor() != null && userIn.getUsername().equals(task.getAuthor().getUser().getUsername())) ||
                userIn.hasRoleAdministrator()) {
            model.addAttribute("iAmAuthor", "true");
        }
        if (task.getUserSelectedTheTask() != null) {
            if (userIn.getUsername().equals(task.getUserSelectedTheTask().getUser().getUsername())) {
                model.addAttribute("iSelectedTask", "true");
                if (task.getStatus().equals(Status.TAKEN)) {
                    model.addAttribute("canMarkCompletedButton", "true");
                }
            }
        }



        return "tasks-details";
    }

    @GetMapping("/tasks/{id}/edit")
    public String tasksEdit(@AuthenticationPrincipal User userIn,
                            @PathVariable(value = "id") long idParam,
                            Model model) {
        if (!taskRepository.existsById(idParam)) {
            return "redirect:/tasks/{id}";
        }
        Task task = taskRepository.findById(idParam).orElseThrow();
        if ((userIn.getUsername().equals(task.getAuthor().getUser().getUsername()))
                || userIn.hasRoleAdministrator()) {
            model.addAttribute("task", task);
        }

        return "tasks-edit";
    }

    @PostMapping("/tasks/{id}/edit")
    public String tasksPostUpdate(@AuthenticationPrincipal User userIn,

                                  @PathVariable(value = "id") long id,
                                  @RequestParam(required = true) String taskName,
                                  @RequestParam(required = false, defaultValue = "") String taskPreview,
                                  @RequestParam(required = false, defaultValue = "") String taskDescription,

                                  @RequestParam(required = false, defaultValue = "0") int daysOfPlannedTime,
                                  @RequestParam(required = false, defaultValue = "0") int hoursOfPlannedTime,
                                  @RequestParam(required = false, defaultValue = "0") int minutesOfPlannedTime,

                                  @RequestParam(required = false, defaultValue = "0") int daysOfActualTime,
                                  @RequestParam(required = false, defaultValue = "0") int hoursOfActualTime,
                                  @RequestParam(required = false, defaultValue = "0") int minutesOfActualTime,

                                  Model model) {

        Task task = taskRepository.findById(id).orElseThrow();

        if (userIn.getUsername().equals(task.getAuthor().getUser().getUsername()) || userIn.hasRoleAdministrator()) {
            Timer plannedTimer = new Timer(daysOfPlannedTime, hoursOfPlannedTime, minutesOfPlannedTime);
            Timer actualTimer = new Timer(daysOfActualTime, hoursOfActualTime, minutesOfActualTime);
            PlannedTime plannedTime = new PlannedTime();
            plannedTime.setTimer(plannedTimer);
            ActualTime actualTime = new ActualTime();
            actualTime.setTimer(actualTimer);

            task.setTaskName(taskName);
            task.setTaskPreview(taskPreview);
            task.setTaskDescription(taskDescription);
            task.setActualTime(actualTime);
            task.setPlannedTime(plannedTime);

            taskRepository.save(task);
        }

        return "redirect:/tasks/" + task.getTaskId();
    }

    @GetMapping("/tasks/{id}/remove")
    public String tasksPostDelete(@AuthenticationPrincipal User userIn,
                                  @PathVariable(value = "id") long id,
                                  Model model) {
        Task task = taskRepository.findById(id).orElseThrow();
        if (userIn.getUsername().equals(task.getAuthor().getUser().getUsername()) || userIn.hasRoleAdministrator()) {
            taskRepository.delete(task);
        }

        return "redirect:/tasks";
    }

    @GetMapping("/tasks/{id}/create_subtask")
    public String tasksGetCreateSubtask(@PathVariable(value = "id") long id, Model model) {
        Task parent = taskRepository.findById(id).orElseThrow();
        model.addAttribute("parent", parent);
        return "tasks-create-subtask";
    }

    @PostMapping("/tasks/{id}/create_subtask")
    public String tasksPostCreateSubtask(@PathVariable(value = "id") long id,
                                         @AuthenticationPrincipal User userIn,
                                         @RequestParam(required = true) String taskName,
                                         @RequestParam(required = false, defaultValue = "") String taskPreview,
                                         @RequestParam(required = false, defaultValue = "") String taskDescription,
                                         Model model) {
        if (!taskRepository.existsById(id)) {
            return "redirect:/tasks";
        }
        Task parent = taskRepository.findById(id).orElseThrow();
        Author author = new Author();
        author.setUser(userIn);
        PlannedTime plannedTime = new PlannedTime();
        plannedTime.setTimer(new Timer());
        ActualTime actualTime = new ActualTime();
        actualTime.setTimer(new Timer());
        List<Status> statuses = new ArrayList<>();
        statuses.add(Status.DISTRIBUTED);

        Task subtask = new Task(
                taskName,
                taskPreview,
                taskDescription,
                0L,
                parent,
                null,
                author,
                null,
                plannedTime,
                actualTime,
                statuses
        );

        taskRepository.save(subtask); // сохранение нового объекта
        return "redirect:/tasks/" + subtask.getTaskId();
    }


    @GetMapping("/tasks/{id}/get_task")
    public String tasksGetGetTask(@PathVariable(value = "id") long id,
                                  @AuthenticationPrincipal User userIn,
                                  Model model) {

        Task task = taskRepository.findById(id).orElseThrow();
        if (task.getUserSelectedTheTask() == null) {
            UserSelectedTheTask userSelectedTheTask = new UserSelectedTheTask();
            userSelectedTheTask.setUser(userIn);
            task.setUserSelectedTheTask(userSelectedTheTask);
            task.setStatus(Status.TAKEN);
            taskRepository.save(task);
        }

        return "redirect:/tasks/{id}";
    }


    @GetMapping("/tasks/{id}/mark_solved")
    public String tasksGetMarkSolved(@PathVariable(value = "id") long id,
                                     @AuthenticationPrincipal User userIn,
                                     Model model) {
        Task task = taskRepository.findById(id).orElseThrow();
        if (task.getUserSelectedTheTask() != null &&
                userIn.getUsername().equals(task.getUserSelectedTheTask().getUser().getUsername()) ||
                userIn.hasRoleAdministrator()) {
            task.setStatus(Status.COMPLETED);
            taskRepository.save(task);
        }

        return "redirect:/tasks/{id}";
    }


    @GetMapping("/tasks/{id}/get_task_cancel")
    public String tasksGetGetTaskCancel(@PathVariable(value = "id") long id,
                                        @AuthenticationPrincipal User userIn,
                                        Model model) {

        Task task = taskRepository.findById(id).orElseThrow();
        if (task.getUserSelectedTheTask() != null &&
                userIn.getUsername().equals(task.getUserSelectedTheTask().getUser().getUsername()) ||
                userIn.hasRoleAdministrator()) {
            userSelectedTheTaskRepository.delete(task.getUserSelectedTheTask());
            task.setUserSelectedTheTask(null);
            task.setStatus(Status.DISTRIBUTED);
            taskRepository.save(task);
        }

        return "redirect:/tasks/{id}";
    }




    @GetMapping("/tasks/{id}/set_planned_time")
    public String tasksGetSetPlannedTime(@AuthenticationPrincipal User userIn,
                                         @PathVariable(value = "id") long id,
                                         Model model) {
        if (!taskRepository.existsById(id)) {
            return "redirect:/tasks";
        }
        Task task = taskRepository.findById(id).orElseThrow();
        model.addAttribute("task", task);

        Task currentTask = taskRepository.findById(id).orElseThrow();
        if (userIn.getUsername().equals(currentTask.getAuthor().getUser().getUsername()) || userIn.hasRoleAdministrator()) {
            model.addAttribute("iAmAuthor", "true");
        }


        return "tasks-set-planned-time";
    }



    @PostMapping("/tasks/{id}/set_planned_time")
    public String tasksPostSetPlannedTime(@AuthenticationPrincipal User userIn,
                                          @PathVariable(value = "id") long id,
                                          @RequestParam(required = false, defaultValue = "0") int daysOfPlannedTime,
                                          @RequestParam(required = false, defaultValue = "0") int hoursOfPlannedTime,
                                          @RequestParam(required = false, defaultValue = "0") int minutesOfPlannedTime,
                                         Model model) {
        if (!taskRepository.existsById(id)) {
            return "redirect:/tasks";
        }
        Task task = taskRepository.findById(id).orElseThrow();
        if (userIn.getUsername().equals(task.getAuthor().getUser().getUsername()) || userIn.hasRoleAdministrator()) {
            Timer timer = new Timer(daysOfPlannedTime, hoursOfPlannedTime, minutesOfPlannedTime);
            PlannedTime plannedTime = new PlannedTime();
            plannedTime.setTimer(timer);
            task.setPlannedTime(plannedTime);

            taskRepository.save(task); // сохранение нового объекта
        }

        return "redirect:/tasks/" + task.getTaskId();
    }


    @GetMapping("/tasks/{id}/change_actual_time")
    public String tasksGetChangeActualTime(@AuthenticationPrincipal User userIn,
                                           @PathVariable(value = "id") long id,
                                           Model model) {
        if (!taskRepository.existsById(id)) {
            return "redirect:/tasks";
        }
        Task task = taskRepository.findById(id).orElseThrow();

        if (task.getUserSelectedTheTask() != null) {
            if (userIn.getUsername().equals(task.getUserSelectedTheTask().getUser().getUsername())  ||
                    userIn.hasRoleAdministrator()) {
                model.addAttribute("iSelectedTask", "true");
            }
        }

        model.addAttribute("task", task);
        return "tasks-change-actual-time";
    }



    @PostMapping("/tasks/{id}/change_actual_time")
    public String tasksPostChangeActualTime(@AuthenticationPrincipal User userIn,
                                            @PathVariable(value = "id") long id,
                                            @RequestParam(required = false, defaultValue = "0") int daysOfActualTime,
                                            @RequestParam(required = false, defaultValue = "0") int hoursOfActualTime,
                                            @RequestParam(required = false, defaultValue = "0") int minutesOfActualTime,
                                          Model model) {
        if (!taskRepository.existsById(id)) {
            return "redirect:/tasks";
        }
        Task task = taskRepository.findById(id).orElseThrow();
        if (task.getUserSelectedTheTask() != null) {
            if (userIn.getUsername().equals(task.getUserSelectedTheTask().getUser().getUsername())  ||
                    userIn.hasRoleAdministrator()) {
                Timer timer = new Timer(daysOfActualTime, hoursOfActualTime, minutesOfActualTime);
                ActualTime actualTime = new ActualTime();
                actualTime.setTimer(timer);
                task.setActualTime(actualTime);

                taskRepository.save(task); // сохранение нового объекта
            }
        }

        return "redirect:/tasks/" + task.getTaskId();
    }



    @GetMapping("/tasks/{id}/add_actual_time")
    public String tasksGetAddActualTime(@AuthenticationPrincipal User userIn,
                                        @PathVariable(value = "id") long id,
                                        Model model) {
        Task task = taskRepository.findById(id).orElseThrow();
        if (task.getUserSelectedTheTask() != null) {
            if (userIn.getUsername().equals(task.getUserSelectedTheTask().getUser().getUsername()) ||
                    userIn.hasRoleAdministrator()) {
                model.addAttribute("iSelectedTask", "true");
            }
        }
        return "tasks-add-actual-time";
    }

    @PostMapping("/tasks/{id}/add_actual_time")
    public String tasksPostAddActualTime(@AuthenticationPrincipal User userIn,
                                         @PathVariable(value = "id") long id,
                                         @RequestParam(required = false, defaultValue = "0") int hoursOfActualTime,
                                         @RequestParam(required = false, defaultValue = "0") int minutesOfActualTime,
                                            Model model) {
        if (!taskRepository.existsById(id)) {
            return "redirect:/tasks";
        }
        Task task = taskRepository.findById(id).orElseThrow();
        if (task.getUserSelectedTheTask() != null) {
            if (userIn.getUsername().equals(task.getUserSelectedTheTask().getUser().getUsername()) ||
                    userIn.hasRoleAdministrator()) {
                task.getActualTime().getTimer().addTime(hoursOfActualTime, minutesOfActualTime);
                taskRepository.save(task); // сохранение нового объекта
            }
        }

        return "redirect:/tasks/" + task.getTaskId();
    }



    private void goDFS(Task task, Timer plannedTimeSum, Timer actualTimeSum, List<Triple> triples, int number, String item) {
        List<Task> children = taskRepository.findChildren(task);
        String newItem = item;
        if (number != 0) {
            newItem = newItem + number + '.';
        }
        Triple triple = new Triple(newItem, task, children.size() > 0 ? false : true);
        triples.add(triple);

        if (children.size() == 0) {
            if (task.getActualTime().getTimer().getTimeAsMinutes() > 0 && task.getPlannedTime().getTimer().getTimeAsMinutes() > 0) {
                plannedTimeSum.addTime(task.getPlannedTime().getTimer().getDays(),task.getPlannedTime().getTimer().getHours(), task.getPlannedTime().getTimer().getMinutes());
                actualTimeSum.addTime(task.getActualTime().getTimer().getDays(), task.getActualTime().getTimer().getHours(), task.getActualTime().getTimer().getMinutes());
            }
        }

        for (int i = 0; i < children.size(); i++) {
            goDFS(children.get(i), plannedTimeSum, actualTimeSum, triples, i+1, newItem);
        }
    }




    @GetMapping("/tasks/{id}/statistics")
    public String tasksGetStatistics(@PathVariable(value = "id") long id,
                                        Model model) {
        if (!taskRepository.existsById(id)) {
            return "redirect:/tasks";
        }
        Timer plannedTimeSum = new Timer();
        Timer actualTimeSum = new Timer();
        List<Triple> triples = new ArrayList<>();

        double plan = 0;
        Task task = taskRepository.findById(id).orElseThrow();
        goDFS(task, plannedTimeSum, actualTimeSum, triples, 0, "1.");


        if (plannedTimeSum.getTimeAsMinutes() > 0) {
            plan = 100.0 * actualTimeSum.getTimeAsMinutes()  / plannedTimeSum.getTimeAsMinutes() ;
        }

        model.addAttribute("triples", triples);
        model.addAttribute("actualTimeSum", actualTimeSum);
        model.addAttribute("plannedTimeSum", plannedTimeSum);
        if (plan < 100) {
            model.addAttribute("successAttribute", true);
        }
        else {
            model.addAttribute("warningAttribute", true);
        }

        model.addAttribute("plan", plan);

        return "tasks-statistics";
    }
}


