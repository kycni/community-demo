package com.song.community.service;

import com.song.community.dao.mapper.LoginTicketMapper;
import com.song.community.dao.mapper.UserMapper;
import com.song.community.entity.LoginTicket;
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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author Kycni
 * @date 2022/2/20 22:45
 */
@Service
public class UserService implements CommunityConstant {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LoginTicketMapper loginTicketMapper;
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

    /**
     * 根据id查找用户 
     */
    public User findUserById (int id) {
        return userMapper.selectUserById(id);
    }

    /**
     * 注册账户
     */
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
     * 激活账户
     * @return 激活状态
     */
    public int activation (int userId, String code) {
        User user = userMapper.selectUserById(userId);
        if (user.getStatus() == 1) {
            return CommunityConstant.ACTIVATION_REPEAT;
        } else if (Objects.equals(user.getActivationCode(), code)) {
            userMapper.updateUserStatus(userId, 1);
            return CommunityConstant.ACTIVATION_SUCCESS;
        } else {
            return CommunityConstant.ACTIVATION_FAILURE;
        }
    }

    /**
     * 登录
     * @return 登录错误信息与登录凭证
     */
    public Map<String, Object> login (String username, String password, int expiredSeconds) {
        HashMap<String, Object> map = new HashMap<>();
        // 空值判断
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        
        // 验证用户名
        User user = userMapper.selectUserByName(username);
        if (user == null) {
            map.put("usernameMsg", "用户名不存在");
            return map;
        }
        // 验证账号激活状态
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "该账号未激活");
            return map;
        }
        // 验证账号密码
        password = communityUtils.md5(password + user.getSalt());
        if (!Objects.equals(password, user.getPassword())) {
            map.put("passwordMsg", "密码不正确");
            return map;
        }
        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(communityUtils.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000L));
        loginTicketMapper.insertLoginTicket(loginTicket);
        map.put("ticket", loginTicket.getTicket());
        return map;
    }
    /**
     * 退出登录
     */
    public void logout (String ticket) {
        loginTicketMapper.updateLoginStatus(ticket,1) ;       
    }

    /**
     * 查找登录凭证
     */
    public LoginTicket findLoginTicket (String ticket) {
        return loginTicketMapper.selectLoginTicket(ticket);
    }

    /**
     * 更新头像
     */
    public int uploadHeader (int userId, String headerUrl) {
        return userMapper.updateHeaderUrl(userId, headerUrl);
    }

    /**
     * 更新密码
     */
    public int changePassword (int userId, String password) {
        return userMapper.updatePassword(userId, password);
        
    }
}
