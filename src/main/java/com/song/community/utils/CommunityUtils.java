package com.song.community.utils;

import com.sun.mail.smtp.DigestMD5;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author Kycni
 * @date 2022/2/21 12:53
 */
@Component
public class CommunityUtils {
    /**
     *  生成随机字符串
     */
    public String generateUUID () {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * md5 数据加密
     * @param key
     * @return
     */
    public String md5 (String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
    }
}
