package com.song.community.utils;

import com.song.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * ThreadLocal持有用户信息，用来代替session对象
 * session对象本身也是线程隔离的
 * session可存放的数据量多，当用户多的时候，浪费服务器资源
 * @author Kycni
 * @date 2022/2/22 18:49
 */
@Component
public class HostHolder {
    
    private ThreadLocal<User> users = new ThreadLocal<>();
    public void setUsers(User user) {
        users.set(user);
    }
    
    public User getUser() {
        return users.get();
    }
    
    public void clear() {
        users.remove();
    }
}
