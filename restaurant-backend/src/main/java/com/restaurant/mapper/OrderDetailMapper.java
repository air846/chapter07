package com.restaurant.mapper;

import com.restaurant.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单详情数据访问接口
 */
@Repository
public interface OrderDetailMapper extends JpaRepository<OrderDetail, Long> {
    
    /**
     * 根据订单ID查询订单详情
     * @param orderId 订单ID
     * @return 订单详情列表
     */
    List<OrderDetail> findByOrderId(Long orderId);
    
    /**
     * 根据菜品ID查询订单详情
     * @param dishId 菜品ID
     * @return 订单详情列表
     */
    List<OrderDetail> findByDishId(Long dishId);
}
