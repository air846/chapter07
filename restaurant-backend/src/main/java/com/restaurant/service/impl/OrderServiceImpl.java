package com.restaurant.service.impl;

import com.restaurant.dto.OrderDto;
import com.restaurant.entity.Order;
import com.restaurant.entity.OrderDetail;
import com.restaurant.entity.Dish;
import com.restaurant.mapper.OrderMapper;
import com.restaurant.mapper.OrderDetailMapper;
import com.restaurant.mapper.DishMapper;
import com.restaurant.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private DishMapper dishMapper;

    @Override
    @Transactional
    public Order submit(OrderDto orderDto) {
        // 创建订单
        Order order = new Order();
        BeanUtils.copyProperties(orderDto, order);
        
        // 生成订单号
        String orderNumber = generateOrderNumber();
        order.setNumber(orderNumber);
        order.setOrderTime(LocalDateTime.now());
        order.setStatus(1); // 待付款
        order.setPayStatus(0); // 未支付
        
        // 计算订单总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        // 保存订单
        Order savedOrder = orderMapper.save(order);
        
        // 保存订单详情
        if (orderDto.getItems() != null) {
            for (OrderDto.OrderItemDto item : orderDto.getItems()) {
                Dish dish = dishMapper.findById(item.getDishId())
                        .orElseThrow(() -> new RuntimeException("菜品不存在"));
                
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderId(savedOrder.getId());
                orderDetail.setDishId(item.getDishId());
                orderDetail.setDishName(dish.getName());
                orderDetail.setPrice(dish.getPrice());
                orderDetail.setNumber(item.getNumber());
                orderDetail.setAmount(dish.getPrice().multiply(BigDecimal.valueOf(item.getNumber())));
                
                orderDetailMapper.save(orderDetail);
                
                totalAmount = totalAmount.add(orderDetail.getAmount());
            }
        }
        
        // 更新订单总金额
        savedOrder.setAmount(totalAmount);
        return orderMapper.save(savedOrder);
    }

    @Override
    public Order getById(Long id) {
        return orderMapper.findById(id)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
    }

    @Override
    public Order getByNumber(String number) {
        Order order = orderMapper.findByNumber(number);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        return order;
    }

    @Override
    public List<Order> getByUserId(Long userId) {
        return orderMapper.findByUserId(userId);
    }

    @Override
    @Transactional
    public boolean updateStatus(Long id, Integer status) {
        Order order = orderMapper.findById(id)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        
        order.setStatus(status);
        orderMapper.save(order);
        
        return true;
    }

    @Override
    @Transactional
    public boolean cancel(Long id) {
        return updateStatus(id, 6); // 6-已取消
    }

    @Override
    @Transactional
    public boolean confirm(Long id) {
        return updateStatus(id, 3); // 3-已接单
    }

    @Override
    @Transactional
    public boolean complete(Long id) {
        return updateStatus(id, 5); // 5-已完成
    }

    @Override
    @Transactional
    public boolean reject(Long id, String reason) {
        Order order = orderMapper.findById(id)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        order.setStatus(6); // 6-已取消
        order.setRemark(reason);
        orderMapper.save(order);
        return true;
    }

    @Override
    @Transactional
    public boolean remove(Long id) {
        Order order = orderMapper.findById(id)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        
        // 删除订单详情
        List<OrderDetail> orderDetails = orderDetailMapper.findByOrderId(id);
        for (OrderDetail detail : orderDetails) {
            orderDetailMapper.delete(detail);
        }
        
        // 删除订单
        orderMapper.delete(order);
        
        return true;
    }

    @Override
    public Page<Order> page(String number, Integer status, Long userId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        return orderMapper.findOrderPage(number, status, userId, startTime, endTime, pageable);
    }

    /**
     * 生成订单号
     */
    private String generateOrderNumber() {
        return "ORDER" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + 
               String.format("%03d", (int)(Math.random() * 1000));
    }
}
