package com.tasktracker.tasks.repo;

import com.tasktracker.tasks.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
