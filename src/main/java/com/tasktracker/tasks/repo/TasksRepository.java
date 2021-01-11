package com.tasktracker.tasks.repo;

import com.tasktracker.tasks.models.Tasks;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TasksRepository extends CrudRepository<Tasks, Long> {
    @Query("SELECT t FROM Tasks t WHERE t.parent_task = :task ORDER BY t.task_id")
    List<Tasks> findChildren(@Param("task") Tasks task);

    @Query("SELECT t FROM Tasks t WHERE t.parent_task = NULL ORDER BY t.task_id")
    List<Tasks> findParents();
}
