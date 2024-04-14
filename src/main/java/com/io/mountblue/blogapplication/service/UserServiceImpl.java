package com.io.mountblue.blogapplication.service;

import com.io.mountblue.blogapplication.dao.UserRepository;
import com.io.mountblue.blogapplication.entity.Role;
import com.io.mountblue.blogapplication.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    UserRepository userRepository;

    UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User findUserById(int id) {
        Optional<User> result = userRepository.findById(id);
        User user = null;

        if (result.isPresent()) {
            user = result.get();
        }
        else {
            throw new RuntimeException("Did not find user id - " + id);
        }
        return user;
    }

    @Override
    public List<User> findAuthors() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public void save(User user) {
        User existingUser = findUserByName(user.getName());
        if(existingUser == null){
            String password = user.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);

            Role role = new Role();
            role.setRole("ROLE_AUTHOR");
            user.setRole(role);
            role.setUsername(user.getName());
            user.setEnabled(true);
            userRepository.save(user);
        }
    }


}
