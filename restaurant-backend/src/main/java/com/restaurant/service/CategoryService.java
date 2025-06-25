package com.restaurant.service;

import com.restaurant.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 菜品分类服务接口
 */
public interface CategoryService {

    /**
     * 新增分类
     * @param category 分类信息
     * @return 新增后的分类
     */
    Category save(Category category);

    /**
     * 根据ID查询分类
     * @param id 分类ID
     * @return 分类信息
     */
    Category getById(Long id);

    /**
     * 更新分类
     * @param category 分类信息
     * @return 更新后的分类
     */
    Category update(Category category);

    /**
     * 删除分类
     * @param id 分类ID
     * @return 是否成功
     */
    boolean remove(Long id);

    /**
     * 查询所有分类
     * @return 分类列表
     */
    List<Category> list();

    /**
     * 分页查询分类
     * @param name 分类名称
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<Category> page(String name, Pageable pageable);
}