package com.song.community.service;

import com.song.community.dao.mapper.DiscussPostMapper;
import com.song.community.entity.DiscussPost;
import com.song.community.utils.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kycni
 * @date 2022/2/20 12:50
 */
@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private Page page;
    /**
     * 找到所有帖子列表并分页
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    public List<DiscussPost> findDiscussPostList (int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPostList(0, page.getOffset(),page.getLimit());
    }

    public int findDiscussPostCount(int userId) {
        return discussPostMapper.selectDiscussPostCount(0);
    }
}
