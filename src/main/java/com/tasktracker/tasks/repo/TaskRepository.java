package com.tasktracker.tasks.repo;

import com.tasktracker.tasks.models.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {

}
