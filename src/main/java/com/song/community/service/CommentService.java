package com.song.community.service;

import com.song.community.dao.mapper.CommentMapper;
import com.song.community.dao.mapper.DiscussPostMapper;
import com.song.community.entity.Comment;
import com.song.community.entity.DiscussPost;
import com.song.community.utils.CommunityConstant;
import com.song.community.utils.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author Kycni
 * @date 2022/2/25 17:26
 */
@Service
public class CommentService implements CommunityConstant {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;
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

    /**
     * 添加评论
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public int addComment (Comment comment) {
        // 对参数进行判空
        if (comment == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        
        // 插入评论
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        int rows = commentMapper.insertComment(comment);
        
        // 更新帖子评论数量
        if (comment.getEntityType() == COMMENT_POST) {
            // 查到插入评论后的最新评论数
            int count = commentMapper.selectCommentCountByEntity(comment.getEntityType(), comment.getEntityId());
            // 将评论数更新
            discussPostMapper.updateCommentCount(comment.getId(), count);
        }
        // 返回插入行数,成功就一行
        return rows;
    }
    
}
