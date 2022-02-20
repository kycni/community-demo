package com.song.community.dao.mapper;

import com.song.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Kycni
 * @date 2022/2/20 8:51
 */
@Mapper
public interface DiscussPostMapper {
    /**
     * 查询带分页功能的帖子列表
     * 思考:什么地方需要帖子列表?:
     *     主页,个人页都需要帖子列表
     *     1. 查询带分页条件的全部帖子列表
     *     2. 查询带分页条件的用户帖子列表
     * 思考:可不可以复用同一个查询方法?
     *     都是查询帖子列表,只是多了一个查询条件,
     *     所以可以复用,可通过动态SQL拼接一个查询条件
     *     这个查询条件是用户的id
     */
    List<DiscussPost> selectDiscussPostList (int userId, int offset, int limit);
}
