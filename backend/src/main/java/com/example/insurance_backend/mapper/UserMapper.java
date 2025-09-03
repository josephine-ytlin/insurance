package com.example.insurance_backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.insurance_backend.model.User;

@Mapper
public interface UserMapper {
	User findByUsername(@Param("username") String username);

    void insertUser(User user);

    User findByToken(@Param("token") String token);

    void updateUser(User user);
}