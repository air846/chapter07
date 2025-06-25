package com.restaurant.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存数据传输对象
 */
@Data
public class InventoryDto {

    private Long id;

    @NotBlank(message = "库存名称不能为空")
    @Size(max = 50, message = "库存名称长度不能超过50个字符")
    private String name;

    @NotNull(message = "库存数量不能为空")
    @DecimalMin(value = "0", message = "库存数量不能小于0")
    private BigDecimal quantity;

    @NotBlank(message = "单位不能为空")
    @Size(max = 10, message = "单位长度不能超过10个字符")
    private String unit;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
    
    // 库存变更记录
    @Data
    public static class InventoryChangeDto {
        
        private Long id;
        
        @NotNull(message = "库存ID不能为空")
        private Long inventoryId;
        
        private String inventoryName;
        
        @NotNull(message = "变更数量不能为空")
        private BigDecimal changeQuantity;
        
        @NotBlank(message = "变更类型不能为空")
        private String changeType; // IN-入库, OUT-出库
        
        @Size(max = 255, message = "备注信息不能超过255个字符")
        private String remark;
        
        private LocalDateTime createTime;
    }
}