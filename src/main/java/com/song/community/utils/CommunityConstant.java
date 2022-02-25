package com.song.community.utils;

/**
 * @author Kycni
 * @date 2022/2/21 15:04
 */
public interface CommunityConstant {
    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;
    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 1;
    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE = 2;

    /**
     * 默认状态有效时间
     */
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;
    
    /**
     * 记住我状态下有效时间
     */
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24;

    /**
     * 评论帖子
     */
    int COMMENT_POST = 1;
    
    /**
     * 评论回复
     */
    int COMMENT_REPLY = 2;
}
