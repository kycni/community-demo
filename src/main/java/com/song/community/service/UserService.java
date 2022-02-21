package com.song.community.service;

import com.song.community.dao.mapper.UserMapper;
import com.song.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kycni
 * @date 2022/2/20 22:45
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    public User findUserById (int id) {
        return userMapper.selectUserById(id);
    }
}
