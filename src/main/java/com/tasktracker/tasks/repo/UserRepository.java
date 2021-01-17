package com.tasktracker.tasks.repo;

import com.tasktracker.tasks.models.Author;
import com.tasktracker.tasks.models.Task;
import com.tasktracker.tasks.models.User;
import com.tasktracker.tasks.models.UserSelectedTheTask;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    /*@Modifying
    @Query("DELETE FROM Task t WHERE t.author = :author")
    void deleteTasksWithAuthor(@Param("author") Author author);*/




}
