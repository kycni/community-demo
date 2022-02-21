package com.song.community;

import com.song.community.utils.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Kycni
 * @date 2022/2/21 11:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityDemoApplication.class)
public class MailSenderTest {
    @Autowired
    private MailClient mailClient;
    
    @Test
    public void sendMessage() {
        mailClient.sendMail("coder417@sina.com", "hello,我的宝宝", "这是一封带点可爱的邮件");    
    }
}
