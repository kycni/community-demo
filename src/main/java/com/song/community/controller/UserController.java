package com.song.community.controller;

import com.song.community.annotation.LoginRequired;
import com.song.community.entity.User;
import com.song.community.service.UserService;
import com.song.community.utils.CommunityUtils;
import com.song.community.utils.CookieUtil;
import com.song.community.utils.HostHolder;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/**
 * @author Kycni
 * @date 2022/2/23 18:57
 */
@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${community.path.upload}")
    private String uploadPath;
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private CommunityUtils communityUtils;

    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }

    /**
     * 上传头像
     */
    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "您还没有选择头像");
            return "/site/setting";
        }
        // 获取用户上传文件名
        String fileName = headerImage.getOriginalFilename();
        // 获取后缀格式
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("uploadHeaderError", "头像格式不正确");
        }
        // 生成随机文件名
        fileName = communityUtils.generateUUID().substring(0, 5) + suffix;
        // 确定文件存放路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            // 将头像存入本地
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败: " + e.getMessage());
            throw new RuntimeException("上传文件失败,服务器发生异常", e);
        }
        // 更新当前用户的头像的路径
        // localhost:7888/community/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/header/" + fileName;
        userService.uploadHeader(user.getId(), headerUrl);

        return "redirect:/index";
    }

    /**
     * 获得头像
     */
    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 从服务器获取
        fileName = uploadPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 相应图片
        response.setContentType("image/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            // 定义游标
            int b = 0;
            // 读输入流
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败 " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 修改密码
     */
    @LoginRequired
    @RequestMapping(path = "/password", method = RequestMethod.POST)
    public String changePassword(HttpServletRequest request, String oldPassword, String newPassword,
                                 String confirmPassword, Model model) {
        // 验证旧密码非空
        if (StringUtils.isBlank(oldPassword)) {
            model.addAttribute("oldPasswordError", "输入的密码不能为空");
            return "site/setting";
        }
        // 验证新密码非空
        if (StringUtils.isBlank(newPassword)) {
            model.addAttribute("newPasswordError", "输入的新密码不能为空");
            return "site/setting";

        }
        if (!Objects.equals(confirmPassword, newPassword)) {
            model.addAttribute("confirmPasswordError", "确认密码不正确");
            return "site/setting";
        }
        
        User user = hostHolder.getUser();
        oldPassword = communityUtils.md5(oldPassword + user.getSalt());
        // 验证旧密码是否正确
        if (!oldPassword.equals(user.getPassword())) {
            model.addAttribute("oldPasswordError", "输入的密码不正确");
            return "site/setting";
        }
        // 加密新密码
        newPassword = communityUtils.md5(newPassword + user.getSalt());
        // 修改密码
        userService.changePassword(user.getId(), newPassword);
        String ticket = CookieUtil.getValue(request, "ticket");
        userService.logout(ticket);
        return "site/login";
    }
}
