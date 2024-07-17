package com.dailyPT.backend.user.service;

import com.dailyPT.backend.common.enums.Role;
import com.dailyPT.backend.common.exception.BadRequestException;
import com.dailyPT.backend.config.JwtTokenProvider;
import com.dailyPT.backend.user.domain.User;
import com.dailyPT.backend.user.repository.jpa.UserJpaRepository;
import com.dailyPT.backend.user.repository.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void addUser(String name, String role) {
        userMapper.addUser(role, name);
    }

    @Transactional
    public List<User> getUser() {
        return userJpaRepository.findAll();
    }

    @Transactional
    public String findById(Long id) {
        User user = userJpaRepository.findById(id).orElseThrow(() -> new BadRequestException("존재하지 않는 사용자 입니다"));
        return jwtTokenProvider.createToken(user.getId(), Role.valueOf(user.getRole()));
    }
}
