package com.song.community.service;

import com.song.community.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Kycni
 * @date 2022/2/27 12:16
 */
@Service
public class LikeService {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 点赞，取消点赞
     */
    public void like (int userId, int entityType, int entityId) {
        // redis通过key操作,操作数据
        String entityLikeKey = RedisUtil.getEntityLikeKey(entityType, entityId);
        // 判断点赞状态，判断用户此前有没有点赞,判断点赞集合里有没有该用户
        Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
        // 更新点赞状态，点过赞,删除点赞状态,没点过赞,添加点赞状态
        if (isMember) {
            redisTemplate.opsForSet().remove(entityLikeKey, userId);
        } else {
            redisTemplate.opsForSet().add(entityLikeKey, userId);
        }
    }

    /**
     * 查询某实体点赞数量(帖子,评论)
     */
    public Long findEntityLikeCount (int entityType,int entityId) {
        String entityLikeKey = RedisUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    /**
     * 查询某人对某实体的点赞状态
     */
    public int findEntityLikeStatus (int userId,int entityType,int entityId) {
        String entityLikeKey = RedisUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }
}
