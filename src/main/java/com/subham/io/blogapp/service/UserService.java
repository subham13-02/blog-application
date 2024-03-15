package com.subham.io.blogapp.service;

import com.subham.io.blogapp.entity.Comment;
import com.subham.io.blogapp.entity.User;

public interface UserService {
    User getUserByPostId(int postId);
}
