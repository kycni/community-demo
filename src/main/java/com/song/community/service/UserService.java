package com.song.community.service;

import com.song.community.dao.mapper.UserMapper;
import com.song.community.entity.User;
import com.song.community.utils.CommunityConstant;
import com.song.community.utils.CommunityUtils;
import com.song.community.utils.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Kycni
 * @date 2022/2/20 22:45
 */
@Service
public class UserService implements CommunityConstant {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MailClient mailClient;
    @Autowired
    private CommunityUtils communityUtils;
    @Autowired
    private TemplateEngine templateEngine;
    @Value("${community.path.domain}")
    private String domain;    
    @Value("${server.servlet.context-path}")
    private String contextPath;
    
    public User findUserById (int id) {
        return userMapper.selectUserById(id);
    }
    
    public Map<String, Object> register (User user) {
        // 判断前端传值
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "用户名不能为空");
            return map;
        }
        
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }        
        User u = userMapper.selectUserByName(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "用户名已存在");
            return map;
        }
        
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }
        u = userMapper.selectUserByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "邮箱已存在");
            return map;
        }
        
        // 插入用户
        user.setSalt(communityUtils.generateUUID().substring(0,5));
        user.setPassword(communityUtils.md5(user.getPassword()+ user.getSalt()));
        user.setStatus(0);
        user.setType(0);
        user.setHeaderUrl(String.format("https://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setActivationCode(communityUtils.generateUUID());
        user.setDateTime(new Date());
        userMapper.insertUser(user);
        
        // 发送验证邮件
        // http://localhost:7888/community/activation/id/code
        Context context = new Context();
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("email", user.getEmail());
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);
        return map;
    }

    /**
     * 
     * @return 集合状态
     */
    public int activation (int userId, String code) {
        User user = userMapper.selectUserById(userId);
        if (user.getStatus() == 1) {
            return CommunityConstant.ACTIVATION_REPEAT;
        } else if (user.getActivationCode() == code) {
            userMapper.updateUserStatus(userId, 1);
            return CommunityConstant.ACTIVATION_SUCCESS;
        } else {
            return CommunityConstant.ACTIVATION_FAILURE;
        }
    }
    
    
    
}
