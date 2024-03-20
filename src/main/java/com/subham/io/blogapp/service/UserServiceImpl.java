package com.subham.io.blogapp.service;

import com.subham.io.blogapp.dao.UserRepository;
import com.subham.io.blogapp.entity.Comment;
import com.subham.io.blogapp.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private UserRepository useRepository;
    public UserServiceImpl(UserRepository useRepository) {
        this.useRepository = useRepository;
    }
    @Override
    public User getUserByPostId(int id) {
        Optional<User> result = useRepository.findById(id);
        User user = null;
        if(result.isPresent()){
            user = result.get();
        }else {
            throw new RuntimeException("No post found with Id: "+id);
        }
        return user;
    }
}
