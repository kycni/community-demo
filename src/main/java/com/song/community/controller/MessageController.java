package com.song.community.controller;

import com.song.community.dao.mapper.MessageMapper;
import com.song.community.entity.Message;
import com.song.community.entity.User;
import com.song.community.service.MessageService;
import com.song.community.service.UserService;
import com.song.community.utils.CommunityUtils;
import com.song.community.utils.HostHolder;
import com.song.community.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

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

    /**
     * 私信详情页
     */
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
        
        // 设置已读
        List<Integer> ids = getLetterIds(letterList);
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }
        return "/site/letter-detail";
    }

    /**
     * 得到私信目标对象 
     */
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

    /**
     * 得到未读集合消息
     */
    private List<Integer> getLetterIds (List<Message> letterList) {
        // 未读消息的Id 集合
        List<Integer> ids = new ArrayList<>();
        if (letterList != null) {
            for (Message letter : letterList) {
                if (hostHolder.getUser().getId() == letter.getToId() && letter.getStatus() == 0) {
                    ids.add(letter.getId());
                }
            }
        }
        return ids;
    }

    /**
     * 发送私信
     */
    @RequestMapping(path = "/letter/send", method = RequestMethod.POST)
    @ResponseBody
    public String sendLetter (String toName, String content) {
        User target = userService.findUserByName(toName);
        if (target == null) {
            return CommunityUtils.getJSONString(1, "目标用户不存在");
        }
        Message message = new Message();
        message.setFromId(hostHolder.getUser().getId());
        message.setContent(content);
        message.setToId(target.getId());
        if (message.getFromId() < message.getToId()) {
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        } else {
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }
        message.setContent(content);
        message.setCreateTime(new Date());
        messageService.addMessage(message);
        
        return CommunityUtils.getJSONString(0);
    }
}
