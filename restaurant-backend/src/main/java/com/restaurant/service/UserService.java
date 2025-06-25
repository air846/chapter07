package com.restaurant.service;

import com.restaurant.dto.UserDto;
import com.restaurant.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return JWT令牌
     */
    String login(String username, String password);

    /**
     * 用户注册
     * @param userDto 用户信息
     * @return 注册成功的用户
     */
    User register(UserDto userDto);

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    User getById(Long id);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    User getByUsername(String username);

    /**
     * 更新用户信息
     * @param userDto 用户信息
     * @return 更新后的用户
     */
    User updateUser(UserDto userDto);

    /**
     * 修改用户密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 启用或禁用用户
     * @param userId 用户ID
     * @param status 状态（0-禁用, 1-启用）
     * @return 是否成功
     */
    boolean updateStatus(Long userId, Integer status);

    /**
     * 分页查询用户
     * @param username 用户名
     * @param name 姓名
     * @param role 角色
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<User> getUserPage(String username, String name, String role, Pageable pageable);

    /**
     * 根据ID删除用户
     * @param id 用户ID
     */
    void deleteById(Long id);

    /**
     * 用户登出
     * @return 是否成功
     */
    boolean logout();
}