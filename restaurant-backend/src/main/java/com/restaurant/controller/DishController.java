package com.restaurant.controller;

import com.restaurant.common.Result;
import com.restaurant.dto.DishDto;
import com.restaurant.entity.Dish;
import com.restaurant.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品控制器
 */
@RestController
@RequestMapping("/api/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 分页查询菜品列表
     */
    @GetMapping("/list")
    public Result<Page<DishDto>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer status) {
        try {
            Pageable pageable = PageRequest.of(page - 1, pageSize);
            Page<DishDto> dishPage = dishService.page(categoryId, name, status, pageable);
            return Result.success(dishPage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据分类ID查询菜品
     */
    @GetMapping("/category/{categoryId}")
    public Result<List<Dish>> getByCategoryId(@PathVariable Long categoryId) {
        try {
            List<Dish> dishes = dishService.listByCategoryId(categoryId);
            return Result.success(dishes);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据ID查询菜品
     */
    @GetMapping("/{id}")
    public Result<DishDto> getById(@PathVariable Long id) {
        try {
            DishDto dish = dishService.getById(id);
            return Result.success(dish);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 新增菜品
     */
    @PostMapping
    public Result<Dish> save(@RequestBody DishDto dishDto) {
        try {
            Dish savedDish = dishService.save(dishDto);
            return Result.success(savedDish);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新菜品
     */
    @PutMapping
    public Result<Dish> update(@RequestBody DishDto dishDto) {
        try {
            Dish updatedDish = dishService.update(dishDto);
            return Result.success(updatedDish);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除菜品
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        try {
            List<Long> ids = List.of(id);
            boolean success = dishService.remove(ids);
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
     * 更新菜品状态
     */
    @PutMapping("/status/{id}")
    public Result<String> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        try {
            List<Long> ids = List.of(id);
            boolean success = dishService.updateStatus(ids, status);
            if (success) {
                return Result.success("状态更新成功");
            } else {
                return Result.error("状态更新失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
