package com.song.community.controller;

import com.song.community.dao.mapper.MessageMapper;
import com.song.community.entity.Message;
import com.song.community.entity.User;
import com.song.community.service.MessageService;
import com.song.community.service.UserService;
import com.song.community.utils.HostHolder;
import com.song.community.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @date 2022/2/26 9:21
 */
@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private Page page;
    @Autowired
    private HostHolder hostHolder;

    /**
     * 私信列表 
     */
    @RequestMapping(path = "/letter/list",method = RequestMethod.GET)
    public String getLetterList (Model model, Page page) {
        User user = hostHolder.getUser();
        if (user != null) {
            // 设置分页信息
            page.setLimit(10);
            page.setPath("/letter/list");
            page.setRows(messageService.findConversationCount(user.getId()));
            
            // 会话列表
            List<Message> conversationList = messageService.findConversations(user.getId(), page.getOffset(), page.getLimit());
            List<Map<String, Object>> conversations = new ArrayList<>();
            for (Message conversation : conversationList) {
                Map<String, Object> messageVo = new HashMap<>();
                messageVo.put("conversation", conversation);
                int letterCount = messageService.findLetterCount(conversation.getConversationId());
                messageVo.put("letterCount", letterCount);
                messageVo.put("unreadCount", messageService.findUnreadLetterCount(conversation.getConversationId(), user.getId()));
                int targetId = user.getId() == conversation.getFromId() ? conversation.getToId() : conversation.getFromId();
                messageVo.put("target", userService.findUserById(targetId));
                conversations.add(messageVo);
            }
            model.addAttribute("conversations", conversations);
            
            // 查询未读消息数量
            int unreadLetterCount = messageService.findUnreadLetterCount(null, user.getId());
            model.addAttribute("letterUnreadCount", unreadLetterCount);
            
        }
        return "site/letter";
    }
    
    @RequestMapping(path = "/letter/detail/{conversationId}", method = RequestMethod.GET)
    public String getLetterDetail (@PathVariable("conversationId") String conversationId,
                                   Page page, Model model) {
        page.setRows(messageService.findLetterCount(conversationId));
        page.setPath("/letter/detail/" + conversationId);
        // 私信列表
        List<Message> letterList = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());
        List<Map<String,Object>> letters = new ArrayList<>();
        for (Message letter : letterList) {
            Map<String, Object> letterVo = new HashMap<>();
            letterVo.put("letter", letter);
            User fromUser = userService.findUserById(letter.getFromId());
            letterVo.put("fromUser", fromUser);
            letters.add(letterVo);
        }
        model.addAttribute("letters", letters);
        
        // 私信目标
        model.addAttribute("target", getLetterTarget(conversationId));
        return "/site/letter-detail";
    }
    
    private User getLetterTarget (String conversationId) {
        String[] ids = conversationId.split("_");
        int id0 = Integer.parseInt(ids[0]);
        int id1 = Integer.parseInt(ids[1]);
        if (hostHolder.getUser().getId() == id0) {
            return userService.findUserById(id0);
        } else {
            return userService.findUserById(id1);
        }
    }
}
