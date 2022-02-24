package com.song.community;

import com.song.community.dao.mapper.LoginTicketMapper;
import com.song.community.entity.LoginTicket;
import com.song.community.utils.CommunityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author Kycni
 * @date 2022/2/21 22:17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityDemoApplication.class)
public class LoginTicketTests {
    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Autowired
    CommunityUtils communityUtils;
    @Test
    public void insertLoginTicket (){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setId(100);
        loginTicket.setUserId(177);
        loginTicket.setStatus(1);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 3600 * 12));
        loginTicket.setTicket(communityUtils.generateUUID());
        loginTicketMapper.insertLoginTicket(loginTicket);
    }
    
    @Test
    public void selectLoginTicket () {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setId(100);
        loginTicket.setUserId(177);
        loginTicket.setStatus(1);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 3600 * 12));
        loginTicket.setTicket(communityUtils.generateUUID());
        loginTicketMapper.selectLoginTicket(loginTicket.getTicket());
    }
}
