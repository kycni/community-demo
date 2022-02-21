package com.song.community.controller;

import com.song.community.entity.User;
import com.song.community.service.UserService;
import com.song.community.utils.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @author Kycni
 * @date 2022/2/21 12:12
 */
@Controller
public class LoginController implements CommunityConstant {
    @Autowired
    private UserService userService;
    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String register () {
        return "/site/register";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage () {
        return "/site/login";
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
}
