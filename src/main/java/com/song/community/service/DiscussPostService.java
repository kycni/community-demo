package com.song.community.service;

import com.song.community.dao.mapper.DiscussPostMapper;
import com.song.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kycni
 * @date 2022/2/20 22:41
 */
@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;
    
    public List<DiscussPost> findDiscussPostList (int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPostList(0, offset, limit);
    }

    public int findDiscussPostCount (@Param("userId") int userId) {
        return discussPostMapper.selectDiscussPostCount(userId);
    }
}
