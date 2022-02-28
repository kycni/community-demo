package com.song.community.utils;

/**
 * @author Kycni
 * @date 2022/2/27 12:10
 */
public class RedisUtil {
    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";

    /**
     * 用户对某个实体的赞，一个赞对应一个用户实体，有几个用户就有几个赞
     * like:entity:entityType:entityId set(userId)
     */
    public static String getEntityLikeKey(int entityType,int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }
    
}
