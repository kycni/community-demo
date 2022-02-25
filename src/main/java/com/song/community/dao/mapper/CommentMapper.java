package com.song.community.dao.mapper;

import com.song.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Kycni
 * @date 2022/2/25 14:15
 */
@Mapper
public interface CommentMapper {
    /**
     * 根据帖子id查询评论
     */
    Comment selectCommentById (int id);

    /**
     * 根据帖子id/评论id查询所有评论列表
     */
    List<Comment> selectCommentByEntityId (int entityType, int entityId, int offset, int limit);
    
    /**
     * 查询评论数
     */
    int selectCommentCountByEntity (int entityType, int entityId);
    
}
