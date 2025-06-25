package com.restaurant.service;

import com.restaurant.dto.OrderDto;
import com.restaurant.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单服务接口
 */
public interface OrderService {

    /**
     * 提交订单
     * @param orderDto 订单信息
     * @return 订单
     */
    Order submit(OrderDto orderDto);

    /**
     * 根据ID查询订单
     * @param id 订单ID
     * @return 订单信息
     */
    Order getById(Long id);

    /**
     * 根据订单号查询订单
     * @param number 订单号
     * @return 订单信息
     */
    Order getByNumber(String number);

    /**
     * 根据用户ID查询订单
     * @param userId 用户ID
     * @return 订单列表
     */
    List<Order> getByUserId(Long userId);

    /**
     * 更新订单状态
     * @param id 订单ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 取消订单
     * @param id 订单ID
     * @return 是否成功
     */
    boolean cancel(Long id);

    /**
     * 确认接单
     * @param id 订单ID
     * @return 是否成功
     */
    boolean confirm(Long id);

    /**
     * 完成订单
     * @param id 订单ID
     * @return 是否成功
     */
    boolean complete(Long id);

    /**
     * 拒绝订单
     * @param id 订单ID
     * @param reason 拒绝原因
     * @return 是否成功
     */
    boolean reject(Long id, String reason);

    /**
     * 删除订单
     * @param id 订单ID
     * @return 是否成功
     */
    boolean remove(Long id);

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
    Page<Order> page(String number, Integer status, Long userId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
}
