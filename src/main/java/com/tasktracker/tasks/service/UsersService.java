package com.tasktracker.tasks.service;

import com.tasktracker.tasks.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsersService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    Users root;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

//    public void checkAdminAccount() {
//        System.out.println("CHECKING FOR ADMIN ACCOUNT");
//
//        Optional<Users> usersOptional = usersRepository.findById(1L);
//
////        Users root = new Users();
////        root.setUsername("root");
////        root.setPassword("$2a$08$ef8vHmCwNhvPz2FVr8CjVev.6bElRL5hN2c2uYpsSNiGWaY6B2PJ6");
////        Set<Roles> set = new HashSet<>();
////        set.add(Roles.USER);
////        set.add(Roles.ADMINISTRATOR);
////        root.setRoles(set);
////        root.setUserId(1L);
//
//        if (usersOptional.isPresent()) {
//            Users user = usersOptional.get();
//            if (!root.equalsdLoginAndPasswordAndRole(user)) {
//                user.setUsername(root.getUsername());
//                user.setPassword(root.getPassword());
//                user.getRoles().addAll(root.getRoles());
//                usersRepository.save(user);
//            }
//        } else {
//            usersRepository.save(root);
//        }
//    }
}
