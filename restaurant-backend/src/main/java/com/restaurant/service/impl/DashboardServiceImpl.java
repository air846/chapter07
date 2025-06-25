package com.restaurant.service.impl;

import com.restaurant.entity.Order;
import com.restaurant.mapper.DishMapper;
import com.restaurant.mapper.OrderMapper;
import com.restaurant.mapper.UserMapper;
import com.restaurant.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 仪表盘服务实现类
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();

        java.time.LocalDateTime startOfDay = java.time.LocalDate.now().atStartOfDay();
        java.time.LocalDateTime endOfDay = java.time.LocalDate.now().atTime(LocalTime.MAX);

        // 今日订单
        java.util.List<Order> todayOrders = orderMapper.findByOrderTimeBetween(startOfDay, endOfDay);
        stats.put("todayOrderCount", todayOrders.size());

        // 今日营业额（已完成订单累计金额）
        java.math.BigDecimal revenue = java.math.BigDecimal.ZERO;
        for (Order o : todayOrders) {
            if (o.getAmount() != null && o.getStatus() != null && o.getStatus() == 5) {
                revenue = revenue.add(o.getAmount());
            }
        }
        stats.put("todayRevenue", revenue);

        // 菜品总数
        stats.put("dishCount", dishMapper.count());

        // 用户总数
        stats.put("userCount", userMapper.count());

        return stats;
    }

    @Override
    public java.util.List<Order> getRecentOrders() {
        Pageable pageable = PageRequest.of(0, 5);
        org.springframework.data.domain.Page<Order> page = orderMapper.findOrderPage(null, null, null, null, null, pageable);
        return page.getContent();
    }

    @Override
    public java.util.List<java.util.Map<String, Object>> getSalesTrend(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        // 默认查询近7天
        if (endDate == null) {
            endDate = java.time.LocalDate.now();
        }
        if (startDate == null) {
            startDate = endDate.minusDays(6);
        }

        java.time.LocalDateTime startDateTime = startDate.atStartOfDay();
        java.time.LocalDateTime endDateTime = endDate.atTime(java.time.LocalTime.MAX);

        // 查询时间范围内的订单
        java.util.List<Order> orders = orderMapper.findByOrderTimeBetween(startDateTime, endDateTime);

        // 按日期统计销售额
        java.util.Map<java.time.LocalDate, java.math.BigDecimal> dailyAmountMap = new java.util.HashMap<>();
        for (Order order : orders) {
            java.time.LocalDate date = order.getOrderTime().toLocalDate();
            java.math.BigDecimal amount = order.getAmount() == null ? java.math.BigDecimal.ZERO : order.getAmount();
            dailyAmountMap.merge(date, amount, java.math.BigDecimal::add);
        }

        // 生成日期列表，确保缺失日期也有数据
        java.util.List<java.util.Map<String, Object>> result = new java.util.ArrayList<>();
        for (java.time.LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            java.util.Map<String, Object> item = new java.util.HashMap<>();
            item.put("date", date.toString());
            item.put("amount", dailyAmountMap.getOrDefault(date, java.math.BigDecimal.ZERO));
            result.add(item);
        }

        return result;
    }

    @Override
    public java.util.List<java.util.Map<String, Object>> getOrderStatusDistribution() {
        java.util.List<Order> orders = orderMapper.findAll();

        // 初始化计数
        int[] counts = new int[7]; // 0 unused, index 1-6
        for (Order order : orders) {
            int status = order.getStatus() == null ? 0 : order.getStatus();
            if (status >= 1 && status <= 6) {
                counts[status]++;
            }
        }

        String[] labels = {"", "待付款", "待接单", "已接单", "派送中", "已完成", "已取消"};
        java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            java.util.Map<String, Object> item = new java.util.HashMap<>();
            item.put("status", i);
            item.put("label", labels[i]);
            item.put("count", counts[i]);
            list.add(item);
        }

        return list;
    }
}
