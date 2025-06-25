package com.restaurant.mapper;

import com.restaurant.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单数据访问接口
 */
@Repository
public interface OrderMapper extends JpaRepository<Order, Long> {
    
    /**
     * 根据订单号查询订单
     * @param number 订单号
     * @return 订单对象
     */
    Order findByNumber(String number);
    
    /**
     * 根据用户ID查询订单
     * @param userId 用户ID
     * @return 订单列表
     */
    List<Order> findByUserId(Long userId);
    
    /**
     * 根据用户ID和状态查询订单
     * @param userId 用户ID
     * @param status 状态
     * @return 订单列表
     */
    List<Order> findByUserIdAndStatus(Long userId, Integer status);
    
    /**
     * 根据状态查询订单
     * @param status 状态
     * @return 订单列表
     */
    List<Order> findByStatus(Integer status);
    
    /**
     * 根据订单时间范围查询订单
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订单列表
     */
    List<Order> findByOrderTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 分页查询订单
     * @param number 订单号
     * @param status 状态
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param pageable 分页参数
     * @return 分页结果
     */
    @Query("SELECT o FROM Order o WHERE (:number IS NULL OR o.number LIKE CONCAT('%', :number, '%')) " +
           "AND (:status IS NULL OR o.status = :status) " +
           "AND (:userId IS NULL OR o.userId = :userId) " +
           "AND (:startTime IS NULL OR o.orderTime >= :startTime) " +
           "AND (:endTime IS NULL OR o.orderTime <= :endTime) " +
           "ORDER BY o.orderTime DESC")
    Page<Order> findOrderPage(
            @Param("number") String number,
            @Param("status") Integer status,
            @Param("userId") Long userId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);
}