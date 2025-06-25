package com.restaurant.controller;

import com.restaurant.common.Result;
import com.restaurant.entity.Category;
import com.restaurant.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品分类控制器
 */
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询分类列表
     */
    @GetMapping("/list")
    public Result<Page<Category>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name) {
        try {
            Pageable pageable = PageRequest.of(page - 1, pageSize);
            Page<Category> categoryPage = categoryService.page(name, pageable);
            return Result.success(categoryPage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询所有分类（不分页）
     */
    @GetMapping("/all")
    public Result<List<Category>> all() {
        try {
            List<Category> categories = categoryService.list();
            return Result.success(categories);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据ID查询分类
     */
    @GetMapping("/{id}")
    public Result<Category> getById(@PathVariable Long id) {
        try {
            Category category = categoryService.getById(id);
            return Result.success(category);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 新增分类
     */
    @PostMapping
    public Result<Category> save(@RequestBody Category category) {
        try {
            Category savedCategory = categoryService.save(category);
            return Result.success(savedCategory);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新分类
     */
    @PutMapping
    public Result<Category> update(@RequestBody Category category) {
        try {
            Category updatedCategory = categoryService.update(category);
            return Result.success(updatedCategory);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        try {
            boolean success = categoryService.remove(id);
            if (success) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
