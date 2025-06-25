package com.restaurant.service.impl;

import com.restaurant.entity.Category;
import com.restaurant.entity.Dish;
import com.restaurant.mapper.CategoryMapper;
import com.restaurant.mapper.DishMapper;
import com.restaurant.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 菜品分类服务实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    
    @Autowired
    private DishMapper dishMapper;

    @Override
    @Transactional
    public Category save(Category category) {
        // 检查分类名称是否已存在
        if (categoryMapper.existsByName(category.getName())) {
            throw new RuntimeException("分类名称已存在");
        }
        
        // 如果没有设置排序，则默认为0
        if (category.getSort() == null) {
            category.setSort(0);
        }
        
        return categoryMapper.save(category);
    }

    @Override
    public Category getById(Long id) {
        return categoryMapper.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
    }

    @Override
    @Transactional
    public Category update(Category category) {
        // 检查分类是否存在
        Category existingCategory = categoryMapper.findById(category.getId())
                .orElseThrow(() -> new RuntimeException("分类不存在"));
        
        // 检查分类名称是否已存在（排除自身）
        Category categoryWithSameName = categoryMapper.findByName(category.getName());
        if (categoryWithSameName != null && !categoryWithSameName.getId().equals(category.getId())) {
            throw new RuntimeException("分类名称已存在");
        }
        
        // 更新分类信息
        existingCategory.setName(category.getName());
        existingCategory.setSort(category.getSort());
        
        return categoryMapper.save(existingCategory);
    }

    @Override
    @Transactional
    public boolean remove(Long id) {
        // 检查分类是否存在
        Category category = categoryMapper.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
        
        // 检查分类下是否有菜品
        List<Dish> dishes = dishMapper.findByCategoryId(id);
        if (!dishes.isEmpty()) {
            throw new RuntimeException("该分类下有菜品，不能删除");
        }
        
        // 删除分类
        categoryMapper.delete(category);
        
        return true;
    }

    @Override
    public List<Category> list() {
        return categoryMapper.findAllByOrderBySortAsc();
    }

    @Override
    public Page<Category> page(String name, Pageable pageable) {
        Category probe = new Category();
        if (name != null) {
            probe.setName(name);
        }
        
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        
        Example<Category> example = Example.of(probe, matcher);
        
        return categoryMapper.findAll(example, pageable);
    }
}