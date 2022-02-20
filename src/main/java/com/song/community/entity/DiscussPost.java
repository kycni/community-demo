package com.song.community.entity;

import java.util.Date;

/**
 * @author Kycni
 * @date 2022/2/20 8:51
 */
@SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
public class DiscussPost {
    private int id;
    private String title;
    private String content;
    // 评论数 
    // 有两种查询方式，从评论表中查，一种是在帖子表中单独设置字段
    // 频繁变化，单独放在discussPost表，不用每次都查询评论表获得）
    private String commentCount;
    // 正常-0，置顶-1，拉黑-2 (管理员设置)
    private int status;
    // 普通-0，精华-1
    private int type;
    private Date createTime;
}
