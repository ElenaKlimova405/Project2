package com.tasktracker.tasks.repo;

import com.tasktracker.tasks.models.Users;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<Users, Long> {
}
