package com.tasktracker.tasks.config;

import com.tasktracker.tasks.models.Roles;
import com.tasktracker.tasks.models.Users;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashSet;
import java.util.Set;

//@Configuration
//public class UsersConfiguration {
//
//    /*
//
//     */
//    @Bean
//    public Users getRoot() {
//        Users root = new Users();
//        root.setUsername("root");
//        root.setPassword("$2a$08$ef8vHmCwNhvPz2FVr8CjVev.6bElRL5hN2c2uYpsSNiGWaY6B2PJ6");
//        Set<Roles> set = new HashSet<>();
//        set.add(Roles.USER);
//        set.add(Roles.ADMINISTRATOR);
//        root.setRoles(set);
//        root.setUserId(1L);
//        return root;
//    }
//}
