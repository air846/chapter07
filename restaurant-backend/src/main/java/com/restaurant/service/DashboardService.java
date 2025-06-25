package com.restaurant.service;

import java.util.Map;

/**
 * 仪表盘服务接口
 */
public interface DashboardService {

    /**
     * 获取仪表盘统计数据
     * @return 统计数据
     */
    Map<String, Object> getStats();

    /**
     * 获取最近订单
     * @return 最近订单列表
     */
    java.util.List<com.restaurant.entity.Order> getRecentOrders();

    /**
     * 获取销售趋势数据（按日期）
     * @param startDate 开始日期 (可为空)
     * @param endDate 结束日期 (可为空)
     * @return 销售趋势列表，每个元素包含 date, amount
     */
    java.util.List<java.util.Map<String, Object>> getSalesTrend(java.time.LocalDate startDate, java.time.LocalDate endDate);

    /**
     * 获取订单状态分布
     * @return 列表，元素包含 status, label, count
     */
    java.util.List<java.util.Map<String, Object>> getOrderStatusDistribution();
}
