package com.restaurant.controller;

import com.restaurant.common.Result;
import com.restaurant.dto.UserDto;
import com.restaurant.entity.User;
import com.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param loginMap 包含用户名和密码的Map
     * @return 登录结果，包含JWT令牌和用户信息
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginMap) {
        String username = loginMap.get("username");
        String password = loginMap.get("password");

        if (username == null || password == null) {
            return Result.error("用户名和密码不能为空");
        }

        try {
            String token = userService.login(username, password);
            User user = userService.getByUsername(username);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("token", token);
            resultMap.put("user", user);
            return Result.success(resultMap);
        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            return Result.error("用户名或密码错误");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户注册
     * @param userDto 用户信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<User> register(@RequestBody UserDto userDto) {
        try {
            User user = userService.register(userDto);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取当前登录用户信息
     * @return 用户信息
     */
    @GetMapping("/info")
    public Result<User> getUserInfo(Authentication authentication) {
        try {
            // 从 Spring Security 上下文中获取当前已认证的用户名
            String username = authentication != null ? authentication.getName() : null;
            if (username == null) {
                return Result.error("未登录，无法获取用户信息");
            }

            User user = userService.getByUsername(username);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 分页查询用户列表
     */
    @GetMapping("/list")
    public Result<Page<User>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String role) {
        try {
            Pageable pageable = PageRequest.of(page - 1, pageSize);
            Page<User> userPage = userService.getUserPage(username, name, role, pageable);
            return Result.success(userPage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        try {
            User user = userService.getById(id);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 新增用户
     */
    @PostMapping
    public Result<User> save(@RequestBody UserDto userDto) {
        try {
            User user = userService.register(userDto);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新用户
     */
    @PutMapping
    public Result<User> update(@RequestBody UserDto userDto) {
        try {
            User user = userService.updateUser(userDto);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        try {
            userService.deleteById(id);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 启用/禁用用户
     */
    @PutMapping("/{id}/status")
    public Result<String> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> statusMap) {
        try {
            Integer status = statusMap.get("status");
            boolean success = userService.updateStatus(id, status);
            if (success) {
                return Result.success("状态更新成功");
            } else {
                return Result.error("状态更新失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result<String> changePassword(@RequestBody Map<String, String> passwordMap) {
        try {
            Long userId = Long.parseLong(passwordMap.get("userId"));
            String oldPassword = passwordMap.get("oldPassword");
            String newPassword = passwordMap.get("newPassword");

            boolean success = userService.changePassword(userId, oldPassword, newPassword);
            if (success) {
                return Result.success("密码修改成功");
            } else {
                return Result.error("密码修改失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户登出
     * @return 登出结果
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        try {
            // 获取当前认证信息
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                // 记录日志
                String username = auth.getName();
                System.out.println("用户 " + username + " 已登出系统");

                // 清除认证信息
                SecurityContextHolder.clearContext();
            }
            return Result.success("退出成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}