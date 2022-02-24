package com.song.community.dao.mapper;

import com.song.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * @author Kycni
 * @date 2022/2/21 21:52
 */
@Mapper
public interface LoginTicketMapper {
    /**
     * 插入登录凭证
     */
    @Insert({
        "insert into login_ticket(user_Id,ticket,status,expired)",
        "values (#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket (LoginTicket loginTicket);

    /**
     * 根据ticket查询登录凭证
     */
    @Select({
            "select id,user_Id,ticket,status,expired ",
            "from login_ticket ",
            "where ticket = #{ticket}"
    })
    LoginTicket selectLoginTicket (String ticket);

    /**
     * 更新登陆状态
     */
    @Update({
            "update login_ticket ",
            "set status = #{status} ",
            "where ticket = #{ticket}"
    })
    int updateLoginStatus (String ticket, int status);
}
