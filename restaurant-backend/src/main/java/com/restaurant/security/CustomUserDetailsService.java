package com.restaurant.security;

import com.restaurant.entity.User;
import com.restaurant.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义UserDetailsService实现，用于加载用户信息
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    // 用户缓存，避免重复查询数据库
    private final Map<String, UserDetails> userCache = new ConcurrentHashMap<>();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 先从缓存中查找
        if (userCache.containsKey(username)) {
            return userCache.get(username);
        }

        // 缓存中没有，从数据库查询
        User user = userMapper.findByUsernameAndStatus(username, 1)
                .orElseThrow(() -> new UsernameNotFoundException("用户名不存在或已被禁用: " + username));

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        // 创建UserDetails对象
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities);

        // 存入缓存
        userCache.put(username, userDetails);

        return userDetails;
    }

    /**
     * 清除指定用户名的缓存，密码/角色变更后需调用
     * @param username 用户名
     */
    public void evictUserCache(String username) {
        if (username == null) return;
        userCache.remove(username);
    }
}
