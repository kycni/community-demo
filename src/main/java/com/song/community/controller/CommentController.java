package com.song.community.controller;

import com.song.community.entity.Comment;
import com.song.community.entity.User;
import com.song.community.service.CommentService;
import com.song.community.utils.CommunityConstant;
import com.song.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @author Kycni
 * @date 2022/2/26 7:29
 */
@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {
    @Autowired
    private CommentService commentService;
    @Autowired
    private HostHolder hostHolder;
    
    @RequestMapping(path = "/add/{discussPost}",method = RequestMethod.POST)
    public String addComment (@PathVariable("discussPost") int discussPost,
                              Comment comment, Model model) {
        User user = hostHolder.getUser();
        if (user != null) {
            comment.setUserId(user.getId());
            comment.setStatus(0);
            comment.setCreateTime(new Date());
            commentService.addComment(comment);
        }
        return "redirect:/discuss/detail/" + discussPost;
    }
}
