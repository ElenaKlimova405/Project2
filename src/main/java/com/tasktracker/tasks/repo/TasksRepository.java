package com.tasktracker.tasks.repo;

import com.tasktracker.tasks.models.Tasks;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TasksRepository extends CrudRepository<Tasks, Long> {
    @Query("SELECT t FROM Tasks t WHERE t.parentTask = :task ORDER BY t.taskId")
    List<Tasks> findChildren(@Param("task") Tasks task);

    @Query("SELECT t FROM Tasks t WHERE t.parentTask = NULL ORDER BY t.taskId")
    List<Tasks> findParents();
}
