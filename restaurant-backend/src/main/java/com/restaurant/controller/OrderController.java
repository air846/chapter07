package com.restaurant.controller;

import com.restaurant.common.Result;
import com.restaurant.dto.OrderDto;
import com.restaurant.entity.Order;
import com.restaurant.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 分页查询订单列表
     */
    @GetMapping("/list")
    public Result<Page<Order>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String number,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        try {
            Pageable pageable = PageRequest.of(page - 1, pageSize);
            Page<Order> orderPage = orderService.page(number, status, userId, startTime, endTime, pageable);
            return Result.success(orderPage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据ID查询订单
     */
    @GetMapping("/{id}")
    public Result<Order> getById(@PathVariable Long id) {
        try {
            Order order = orderService.getById(id);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据订单号查询订单
     */
    @GetMapping("/number/{number}")
    public Result<Order> getByNumber(@PathVariable String number) {
        try {
            Order order = orderService.getByNumber(number);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 提交订单
     */
    @PostMapping("/submit")
    public Result<Order> submit(@RequestBody OrderDto orderDto) {
        try {
            Order order = orderService.submit(orderDto);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新订单状态
     */
    @PutMapping("/status/{id}")
    public Result<String> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        try {
            boolean success = orderService.updateStatus(id, status);
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
     * 取消订单
     */
    @PutMapping("/{id}/cancel")
    public Result<String> cancel(@PathVariable Long id) {
        try {
            boolean success = orderService.cancel(id);
            if (success) {
                return Result.success("订单取消成功");
            } else {
                return Result.error("订单取消失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 确认接单
     */
    @PutMapping("/{id}/confirm")
    public Result<String> confirm(@PathVariable Long id) {
        try {
            boolean success = orderService.confirm(id);
            if (success) {
                return Result.success("接单成功");
            } else {
                return Result.error("接单失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 完成订单
     */
    @PutMapping("/{id}/complete")
    public Result<String> complete(@PathVariable Long id) {
        try {
            boolean success = orderService.complete(id);
            if (success) {
                return Result.success("订单完成");
            } else {
                return Result.error("操作失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 拒绝订单
     */
    @PutMapping("/{id}/reject")
    public Result<String> reject(@PathVariable Long id, @RequestBody(required = false) Map<String, String> body) {
        try {
            String rejectReason = body != null ? body.get("reason") : null;
            boolean success = orderService.reject(id, rejectReason);
            if (success) {
                return Result.success("订单已拒绝");
            } else {
                return Result.error("操作失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除订单
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        try {
            boolean success = orderService.remove(id);
            if (success) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取用户订单
     */
    @GetMapping("/user/{userId}")
    public Result<List<Order>> getUserOrders(@PathVariable Long userId) {
        try {
            List<Order> orders = orderService.getByUserId(userId);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
