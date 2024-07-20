package com.dailyPT.backend.user.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    void addUser(@Param("role") String role, @Param("name") String name);
}