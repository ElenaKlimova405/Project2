package com.tasktracker.tasks.repo;

import com.tasktracker.tasks.models.Tasks;
import org.springframework.data.repository.CrudRepository;

public interface TasksRepository extends CrudRepository<Tasks, Long> {
}
