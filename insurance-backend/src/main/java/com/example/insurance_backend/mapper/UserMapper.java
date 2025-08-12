package com.example.insurance_backend.mapper;

import com.example.insurance_backend.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
	User findByUsername(@Param("username") String username);

    void insertUser(User user);

    User findByToken(@Param("token") String token);

    void updateUser(User user);
}