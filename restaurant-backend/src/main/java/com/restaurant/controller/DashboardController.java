package com.restaurant.controller;

import com.restaurant.common.Result;
import com.restaurant.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 仪表盘控制器
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * 获取仪表盘统计数据
     */
    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        try {
            Map<String, Object> stats = dashboardService.getStats();
            return Result.success(stats);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取最近订单
     */
    @GetMapping("/recent-orders")
    public Result<java.util.Map<String, Object>> getRecentOrders() {
        try {
            java.util.List<com.restaurant.entity.Order> recentOrders = dashboardService.getRecentOrders();
            java.util.List<java.util.Map<String, Object>> records = new java.util.ArrayList<>();
            for (com.restaurant.entity.Order o : recentOrders) {
                java.util.Map<String, Object> r = new java.util.HashMap<>();
                r.put("orderNumber", o.getNumber());
                r.put("amount", o.getAmount());
                r.put("status", o.getStatus());
                r.put("orderTime", o.getOrderTime());
                // 取用户名
                String customer = o.getUser() != null ? o.getUser().getUsername() : "";
                r.put("customerName", customer);
                records.add(r);
            }
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("records", records);
            return Result.success(map);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 销售趋势
     */
    @GetMapping("/sales-trend")
    public Result<java.util.List<java.util.Map<String, Object>>> getSalesTrend(
            @RequestParam(required = false) java.time.LocalDate startDate,
            @RequestParam(required = false) java.time.LocalDate endDate) {
        try {
            java.util.List<java.util.Map<String, Object>> list = dashboardService.getSalesTrend(startDate, endDate);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 订单状态分布
     */
    @GetMapping("/order-status-distribution")
    public Result<java.util.List<java.util.Map<String, Object>>> getOrderStatusDistribution() {
        try {
            java.util.List<java.util.Map<String, Object>> list = dashboardService.getOrderStatusDistribution();
            return Result.success(list);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
