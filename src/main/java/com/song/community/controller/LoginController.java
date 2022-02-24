package com.song.community.controller;

import com.google.code.kaptcha.Producer;
import com.song.community.entity.LoginTicket;
import com.song.community.entity.User;
import com.song.community.service.UserService;
import com.song.community.utils.CommunityConstant;
import com.song.community.utils.CommunityUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.util.DateUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kycni
 * @date 2022/2/21 12:12
 */
@Controller
public class LoginController implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private Producer kaptchaProducer;
    @Autowired
    private CommunityUtils communityUtils;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    
    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String register () {
        return "/site/register";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage () {
        return "/site/login";
    }
    
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login (String username, String password, String code, Model model,boolean rememberMe,
                                      HttpSession session, HttpServletResponse response) {
        String kaptcha = (String) session.getAttribute("kaptcha");
        // 验证验证码
        if (StringUtils.isBlank(code) || StringUtils.isBlank(kaptcha) || !kaptcha.equalsIgnoreCase(code)) {
            model.addAttribute("codeMsg", "验证码不正确");
            return "site/login";
        }
        // 验证账号，密码
        int expiredSeconds = rememberMe ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if (map.containsKey("ticket")) {
            // 账号，密码验证成功
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setMaxAge(expiredSeconds);
            cookie.setPath(contextPath);
            response.addCookie(cookie);
            return "redirect:/index";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
        }
        return "/site/login";
    }
    
    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha (HttpServletResponse response, HttpSession session) {
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);
        // 将验证码存入session
        session.setAttribute("kaptcha", text);
        // 将验证图片response返回
        response.setContentType("image/png");
        try {
             OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
             logger.error("验证码异常" + e.getMessage());
        }
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register (Model model, User user) {
        Map<String, Object> map = userService.register(user);
        // 注册成功,由map代表有错误,无map代表无错误
        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "注册成功!我们已经向您的邮箱发送了一条激活链接,请尽快前去激活");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }
    }

    // http://localhost:7888/community/activation/id/code
    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation (Model model, 
                              @PathVariable("userId") int userId, 
                              @PathVariable("code") String code) {
        int result = userService.activation(userId, code);
        
        if (result == CommunityConstant.ACTIVATION_REPEAT) {
            model.addAttribute("msg", "已经激活成功，切勿重复激活");
            model.addAttribute("target", "/index");
        } else if (result == CommunityConstant.ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "恭喜您，激活成功!");
            model.addAttribute("target", "/login");
        } else {
            model.addAttribute("msg", "激活失败，您提供的激活码不正确");
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";
    }

    /**
     * 退出登录
     */
    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout (@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/login";
    }
}
