package com.song.community.controller;

import com.song.community.entity.User;
import com.song.community.service.LikeService;
import com.song.community.utils.CommunityUtils;
import com.song.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kycni
 * @date 2022/2/27 12:31
 */
@Controller
public class LikeController {
    @Autowired
    private LikeService likeService;
    @Autowired
    private HostHolder hostHolder;

    /**
     * 异步请求 点赞
     */
    @RequestMapping(path = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String like (int entityType, int entityId) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtils.getJSONString(1, "null");
        }
        // 实现点赞
        likeService.like(user.getId(), entityType, entityId);
        // 点赞数
        Long likeCount = likeService.findEntityLikeCount(entityType, entityId);
        // 点赞状态
        int likeStatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);
        // 点赞结果
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);
        // 向点赞结果返回给前端
        return CommunityUtils.getJSONString(0, "null",map);
    }
}
