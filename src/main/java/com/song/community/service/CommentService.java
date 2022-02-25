package com.song.community.service;

import com.song.community.dao.mapper.CommentMapper;
import com.song.community.entity.Comment;
import com.song.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kycni
 * @date 2022/2/25 17:26
 */
@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    /**
     * 显示评论的列表
     */
    public List<Comment> findCommentList (int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentByEntityId(entityType,entityId, offset, limit);
    }
    
    /**
     * 查询评论数
     */
    public int findCommentCount (int entityType, int entityId) {
        return commentMapper.selectCommentCountByEntity(entityType, entityId);
    }
    
}
