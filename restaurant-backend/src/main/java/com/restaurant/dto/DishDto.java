package com.restaurant.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 菜品数据传输对象
 */
@Data
public class DishDto {

    private Long id;

    @NotBlank(message = "菜品名称不能为空")
    @Size(max = 50, message = "菜品名称长度不能超过50个字符")
    private String name;

    @NotNull(message = "菜品分类不能为空")
    private Long categoryId;

    private String categoryName;

    @NotNull(message = "菜品价格不能为空")
    @DecimalMin(value = "0.01", message = "菜品价格必须大于0")
    private BigDecimal price;

    private String image;

    @Size(max = 255, message = "描述信息不能超过255个字符")
    private String description;

    private Integer status; // 0-停售, 1-起售
}