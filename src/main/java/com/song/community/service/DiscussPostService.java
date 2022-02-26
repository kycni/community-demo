package com.song.community.service;

import com.song.community.dao.mapper.DiscussPostMapper;
import com.song.community.entity.DiscussPost;
import com.song.community.utils.CommunityUtils;
import com.song.community.utils.SensitiveFilter;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author Kycni
 * @date 2022/2/20 22:41
 */
@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    /**
     * 帖子列表
     */    
    public List<DiscussPost> findDiscussPostList (int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPostList(0, offset, limit);
    }

    /**
     * 帖子数
     */
    public int findDiscussPostCount (@Param("userId") int userId) {
        return discussPostMapper.selectDiscussPostCount(userId);
    }

    /**
     * 发帖
     */
    public int addDiscussPost (DiscussPost discussPost) {
        
        // 对参数进行判断
        if (discussPost == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        
        // 转义Html标记
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));
        
        // 过滤敏感词
        discussPost.setTitle(sensitiveFilter.filter(discussPost.getTitle()));
        discussPost.setContent(sensitiveFilter.filter(discussPost.getContent()));
        
        return discussPostMapper.insertDiscussPost(discussPost);
    }

    /**
     * 通过帖子id查找帖子
     */
    public DiscussPost findDiscussPost (int id) {
        return discussPostMapper.selectDiscussPost(id);
    }

    /**
     * 增加评论数
     */
    public int addCommentCount (int id, int commentCount) {
        return discussPostMapper.updateCommentCount(id,commentCount);
    }
}
