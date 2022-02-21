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
     * 动态SQL
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<DiscussPost> selectDiscussPostList(int userId, int offset,int limit);
    
    /**
     *     动态参数，如果唯一，那么我们就得加上这个标签
     */
    int selectDiscussPostCount (@Param("userId") int userId);
    
}
