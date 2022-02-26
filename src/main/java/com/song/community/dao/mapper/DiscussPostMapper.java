package com.song.community.dao.mapper;

import com.song.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Kycni
 * @date 2022/2/20 21:42
 */
@Mapper
public interface DiscussPostMapper {
    /**
     * 查询帖子列表,带分页
     * 动态SQL,可查全部帖子和个人发帖
     */
    List<DiscussPost> selectDiscussPostList(int userId, int offset,int limit);
    
    /**
     *     动态参数，如果唯一，那么我们就得加上这个标签
     */
    int selectDiscussPostCount (@Param("userId") int userId);

    /**
     * 插入帖子
     */
    int insertDiscussPost (DiscussPost discussPost);
    
    /**
     * 根据帖子id查询帖子信息
     */
    DiscussPost selectDiscussPost (int id);

    /**
     * 修改评论数
     */
    int updateCommentCount (int id, int commentCount);
}
