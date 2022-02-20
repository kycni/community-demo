package com.song.community.controller;

import com.mysql.cj.util.StringUtils;
import com.song.community.entity.DiscussPost;
import com.song.community.entity.User;
import com.song.community.service.DiscussPostService;
import com.song.community.service.UserService;
import com.song.community.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kycni
 * @date 2022/2/20 7:53
 */
@Controller
public class HomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private DiscussPostService discussPostService;
    @Value("/community")
    private static String CONTEXT_PATH;
    /**
     * 拆分帖子列表
     * 将单独帖子与对应发帖人信息组合
     * 组合新的帖子列表
     * 打包发回前端
     * discussPostList -> post + user -> discussPostVo -> discussPostVoList
     * @return
     */

    @RequestMapping(path = "/index/{current}", method = RequestMethod.GET)
    public String index (@PathVariable(name = "current") int current, Page page, Model model) {
        // 设置分页结构
        page.setCurrent(current);
        page.setPath(CONTEXT_PATH + "/" + current);
        page.setLimit(10);
        page.setRows(discussPostService.findDiscussPostCount(0));
        
        // 得到全部帖子列表
        List<DiscussPost> discussPostList = discussPostService.findDiscussPostList(0, page.getOffset(), page.getLimit());
        // 判断帖子列表是否为空,如果为空则会报空指针异常
        if (discussPostList == null) {
            throw new NullPointerException("帖子列表为空");
        }
        List<Map<String, Object>> discussPostVoList = new ArrayList<>();
        
        // 得到单独帖子列表
        for (DiscussPost discussPost : discussPostList) {
            // 组装帖子列表和用户信息
            Map<String, Object> discussPostVo = new HashMap<>();
            discussPostVo.put("post", discussPost);
            discussPostVo.put("user", userService.findUserById(discussPost.getUserId()));
            // 将组装好的信息添加到新的帖子列表
            discussPostVoList.add(discussPostVo);
        }
        // 返回新帖子列表
        model.addAttribute("posts", discussPostVoList);
        return "/index";
    }
}
