package com.song.community.service;

import com.song.community.dao.mapper.DiscussPostMapper;
import com.song.community.dao.mapper.UserMapper;
import com.song.community.entity.DiscussPost;
import com.song.community.entity.User;
import com.song.community.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kycni
 * @date 2022/2/20 10:32
 */
@Service
public class HomeService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private Page page;
    
    // 找到所有帖子列表并分页
    public List<DiscussPost> findDiscussPostList (int userId,int offset, int limit) {
        return discussPostMapper.selectDiscussPostList(0, page.getOffset(),page.getLimit());
    }
    
    // 根据userId找到具体的用户
    public User findUserById(int userId) {
        return userMapper.selectUserById(userId);
    } 
}
