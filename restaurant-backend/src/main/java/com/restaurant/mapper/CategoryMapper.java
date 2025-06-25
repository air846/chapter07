package com.restaurant.mapper;

import com.restaurant.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜品分类数据访问接口
 */
@Repository
public interface CategoryMapper extends JpaRepository<Category, Long> {
    
    /**
     * 根据名称查询分类
     * @param name 分类名称
     * @return 分类对象
     */
    Category findByName(String name);
    
    /**
     * 检查分类名称是否存在
     * @param name 分类名称
     * @return 是否存在
     */
    boolean existsByName(String name);
    
    /**
     * 按照排序字段升序查询所有分类
     * @return 分类列表
     */
    List<Category> findAllByOrderBySortAsc();
}