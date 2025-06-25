package com.restaurant.service.impl;

import com.restaurant.common.JwtUtil;
import com.restaurant.dto.UserDto;
import com.restaurant.entity.User;
import com.restaurant.mapper.UserMapper;
import com.restaurant.service.UserService;
import com.restaurant.security.CustomUserDetailsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public String login(String username, String password) {
        // 使用Spring Security进行认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 生成JWT令牌
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtUtil.generateToken(userDetails);
    }

    @Override
    @Transactional
    public User register(UserDto userDto) {
        // 检查用户名是否已存在
        if (userMapper.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 创建新用户
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        
        // 设置默认值
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if (user.getRole() == null) {
            user.setRole("STAFF"); // 默认角色
        }
        if (user.getStatus() == null) {
            user.setStatus(1); // 默认启用
        }
        
        return userMapper.save(user);
    }

    @Override
    public User getById(Long id) {
        return userMapper.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    @Override
    public User getByUsername(String username) {
        return userMapper.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    @Override
    @Transactional
    public User updateUser(UserDto userDto) {
        User user = userMapper.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 更新用户信息，但不更新密码和角色
        user.setName(userDto.getName());
        user.setPhone(userDto.getPhone());
        user.setEmail(userDto.getEmail());
        
        return userMapper.save(user);
    }

    @Override
    @Transactional
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("旧密码不正确");
        }
        
        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.save(user);
        
        // 清除缓存，避免旧密码仍被缓存导致后续登录失败
        customUserDetailsService.evictUserCache(user.getUsername());
        
        return true;
    }

    @Override
    @Transactional
    public boolean updateStatus(Long userId, Integer status) {
        User user = userMapper.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        user.setStatus(status);
        userMapper.save(user);
        
        return true;
    }

    @Override
    public Page<User> getUserPage(String username, String name, String role, Pageable pageable) {
        User probe = new User();
        if (username != null) {
            probe.setUsername(username);
        }
        if (name != null) {
            probe.setName(name);
        }
        if (role != null) {
            probe.setRole(role);
        }

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        Example<User> example = Example.of(probe, matcher);

        return userMapper.findAll(example, pageable);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!userMapper.existsById(id)) {
            throw new RuntimeException("用户不存在");
        }
        userMapper.deleteById(id);
    }

    /**
     * 用户登出
     * @return 是否成功
     */
    public boolean logout() {
        try {
            SecurityContextHolder.clearContext();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}