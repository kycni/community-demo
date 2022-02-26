package com.song.community.service;

import com.song.community.dao.mapper.MessageMapper;
import com.song.community.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kycni
 * @date 2022/2/26 9:15
 */
@Service
public class MessageService {
    @Autowired
    private MessageMapper messageMapper;
    
    public List<Message> findConversations (int userId, int offset, int limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }
    
    public List<Message> findLetters (String conversationId, int offset, int limit) {
        return messageMapper.selectLetters(conversationId,offset,limit);
    }
    
    public int findConversationCount (int userId) {
        return messageMapper.selectConversationCount(userId);
    }
    
    public int findLetterCount (String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }
    
    public int findUnreadLetterCount (String conversationId,int userId) {
        return messageMapper.selectUnreadLetterCount(conversationId, userId);
    }
}
