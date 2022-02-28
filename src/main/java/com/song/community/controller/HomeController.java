package com.song.community.controller;

import com.song.community.entity.DiscussPost;
import com.song.community.service.DiscussPostService;
import com.song.community.service.LikeService;
import com.song.community.service.UserService;
import com.song.community.utils.CommunityConstant;
import com.song.community.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sun.security.krb5.internal.PAForUserEnc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kycni
 * @date 2022/2/20 21:24
 */
@Controller
public class HomeController implements CommunityConstant {
    @Autowired
    private UserService userService;
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private Page page;
    
    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndexPage (Model model, Page page) {
        
        page.setRows(discussPostService.findDiscussPostCount(0));
        page.setPath("/index");
        
        List<DiscussPost> discussPostList = discussPostService.findDiscussPostList(0, page.getOffset(), page.getLimit());
        
        List<Map<String ,Object>> discussPostVoList = new ArrayList<>();
        if (discussPostList != null) {
            for (DiscussPost discussPost : discussPostList) {
                Map<String,Object> map = new HashMap<>();
                map.put("post", discussPost);
                map.put("user", userService.findUserById(discussPost.getUserId()));
                Long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPost.getId());
                map.put("likeCount", likeCount);
                discussPostVoList.add(map);
            }
        }
        
        model.addAttribute("discussPosts", discussPostVoList);
        return "/index";
    }

    /**
     * 错误页
     */
    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public String getErrorPage () {
        return "/error/500";
    }
}
