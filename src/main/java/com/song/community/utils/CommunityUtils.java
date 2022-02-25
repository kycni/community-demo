package com.song.community.utils;

import com.alibaba.fastjson.JSONObject;
import com.sun.mail.smtp.DigestMD5;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
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
     */
    public String md5 (String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 返回Json字符串
     */
    public static String getJSONString (int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map!=null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    public static String getJSONString (int code, String msg) {
        return getJSONString(code, msg, null);
    }

    public static String getJSONString (int code) {
        return getJSONString(code, null, null);
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("姓名", "宋寅琪");
        map.put("年龄", 25);
        System.out.println(getJSONString(0, "ok", map));
    }
}
