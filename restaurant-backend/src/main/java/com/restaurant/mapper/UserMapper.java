package com.restaurant.mapper;

import com.restaurant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户数据访问接口
 */
@Repository
public interface UserMapper extends JpaRepository<User, Long> {
    
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 根据用户名和状态查询用户
     * @param username 用户名
     * @param status 状态（0-禁用, 1-启用）
     * @return 用户对象
     */
    Optional<User> findByUsernameAndStatus(String username, Integer status);
    
    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);
}