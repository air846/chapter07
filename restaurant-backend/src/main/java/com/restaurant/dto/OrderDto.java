package com.restaurant.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单数据传输对象
 */
@Data
public class OrderDto {

    private Long id;

    private String number; // 订单号

    private Integer status; // 1-待付款, 2-待接单, 3-已接单, 4-派送中, 5-已完成, 6-已取消

    private Long userId;

    private String userName;

    @NotNull(message = "收货地址不能为空")
    private Long addressId;

    private LocalDateTime orderTime;

    @NotNull(message = "支付方式不能为空")
    private Integer payMethod; // 1-微信, 2-支付宝

    private Integer payStatus; // 0-未支付, 1-已支付

    private BigDecimal amount;

    @Size(max = 255, message = "备注信息不能超过255个字符")
    private String remark;

    @NotEmpty(message = "订单项不能为空")
    @Valid
    private List<OrderItemDto> items;

    /**
     * 订单项数据传输对象
     */
    @Data
    public static class OrderItemDto {
        
        private Long id;
        
        @NotNull(message = "菜品ID不能为空")
        private Long dishId;
        
        private String dishName;
        
        private BigDecimal price;
        
        @NotNull(message = "数量不能为空")
        private Integer number;
        
        private BigDecimal amount;
    }
}