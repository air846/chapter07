package com.restaurant.service.impl;

import com.restaurant.dto.DishDto;
import com.restaurant.entity.Category;
import com.restaurant.entity.Dish;
import com.restaurant.mapper.CategoryMapper;
import com.restaurant.mapper.DishMapper;
import com.restaurant.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 菜品服务实现类
 */
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    @Transactional
    public Dish save(DishDto dishDto) {
        // 转换DTO为实体
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto, dish);
        
        // 设置默认状态为停售
        if (dish.getStatus() == null) {
            dish.setStatus(0);
        }
        
        // 保存菜品
        return dishMapper.save(dish);
    }

    @Override
    public DishDto getById(Long id) {
        // 查询菜品
        Dish dish = dishMapper.findById(id)
                .orElseThrow(() -> new RuntimeException("菜品不存在"));
        
        // 转换为DTO
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setImage(resolveImagePath(dish.getImage()));
        
        // 查询分类名称
        if (dish.getCategoryId() != null) {
            Category category = categoryMapper.findById(dish.getCategoryId()).orElse(null);
            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }
        }
        
        return dishDto;
    }

    @Override
    @Transactional
    public Dish update(DishDto dishDto) {
        // 检查菜品是否存在
        Dish dish = dishMapper.findById(dishDto.getId())
                .orElseThrow(() -> new RuntimeException("菜品不存在"));
        
        // 更新菜品信息
        BeanUtils.copyProperties(dishDto, dish);
        
        return dishMapper.save(dish);
    }

    @Override
    @Transactional
    public boolean remove(List<Long> ids) {
        // 检查菜品是否存在
        for (Long id : ids) {
            dishMapper.findById(id)
                    .orElseThrow(() -> new RuntimeException("菜品不存在"));
        }
        
        // 删除菜品
        for (Long id : ids) {
            dishMapper.deleteById(id);
        }
        
        return true;
    }

    @Override
    public List<Dish> listByCategoryId(Long categoryId) {
        return dishMapper.findByCategoryId(categoryId);
    }

    @Override
    public List<Dish> listByCategoryIdAndStatus(Long categoryId, Integer status) {
        return dishMapper.findByCategoryIdAndStatus(categoryId, status);
    }

    @Override
    @Transactional
    public boolean updateStatus(List<Long> ids, Integer status) {
        // 检查菜品是否存在
        for (Long id : ids) {
            Dish dish = dishMapper.findById(id)
                    .orElseThrow(() -> new RuntimeException("菜品不存在"));
            
            // 更新状态
            dish.setStatus(status);
            dishMapper.save(dish);
        }
        
        return true;
    }

    @Override
    public Page<DishDto> page(Long categoryId, String name, Integer status, Pageable pageable) {
        // 查询菜品
        Page<Dish> dishPage = dishMapper.findDishPage(categoryId, name, status, pageable);
        
        // 转换为DTO
        List<DishDto> dishDtoList = new ArrayList<>();
        for (Dish dish : dishPage.getContent()) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            dishDto.setImage(resolveImagePath(dish.getImage()));
            
            // 查询分类名称
            if (dish.getCategoryId() != null) {
                Category category = categoryMapper.findById(dish.getCategoryId()).orElse(null);
                if (category != null) {
                    dishDto.setCategoryName(category.getName());
                }
            }
            
            dishDtoList.add(dishDto);
        }
        
        return new PageImpl<>(dishDtoList, pageable, dishPage.getTotalElements());
    }

    /**
     * 解决旧数据图片路径兼容
     */
    private String resolveImagePath(String image) {
        if (!StringUtils.hasText(image)) {
            return image;
        }
        if (image.startsWith("/") || image.startsWith("http")) {
            return image;
        }
        return "/static/images/" + image;
    }
}