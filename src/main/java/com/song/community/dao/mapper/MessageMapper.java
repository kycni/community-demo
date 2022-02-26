package com.song.community.dao.mapper;

import com.song.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Kycni
 * @date 2022/2/26 8:22
 */
@Mapper
public interface MessageMapper {
    /**
     * 查询当前用户的会话列表
     */
    List<Message> selectConversations (int userId, int offset, int limit);

    /**
     * 查询当前用户会话的数量
     */
    int selectConversationCount (int userId);

    /**
     * 查询某个会话所包含的私信列表
     */
    List<Message> selectLetters (String conversationId,int offset,int limit);

    /**
     * 查询某个会话所包含的私信数量
     */
    int selectLetterCount (String conversationId);

    /**
     * 查询当前用户的未读消息数量
     */
    int selectUnreadLetterCount (String conversationId, int userId);
}

