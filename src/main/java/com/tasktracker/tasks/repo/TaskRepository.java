package com.tasktracker.tasks.repo;

import com.tasktracker.tasks.models.Author;
import com.tasktracker.tasks.models.Task;
import com.tasktracker.tasks.models.UserSelectedTheTask;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.parentTask = :task ORDER BY t.taskId")
    List<Task> findChildren(@Param("task") Task task);

    @Query("SELECT t FROM Task t WHERE t.parentTask = NULL ORDER BY t.taskId")
    List<Task> findParents();

    Task findByAuthor(Author author);

    Task findByUserSelectedTheTask(UserSelectedTheTask userSelectedTheTask);
}
