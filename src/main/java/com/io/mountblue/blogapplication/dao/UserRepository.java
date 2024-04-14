package com.io.mountblue.blogapplication.dao;

import com.io.mountblue.blogapplication.entity.User;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    public User findByName(String name);
}
