package com.restaurant.service;

import com.restaurant.dto.DishDto;
import com.restaurant.entity.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 菜品服务接口
 */
public interface DishService {

    /**
     * 新增菜品
     * @param dishDto 菜品信息
     * @return 新增后的菜品
     */
    Dish save(DishDto dishDto);

    /**
     * 根据ID查询菜品
     * @param id 菜品ID
     * @return 菜品信息
     */
    DishDto getById(Long id);

    /**
     * 更新菜品
     * @param dishDto 菜品信息
     * @return 更新后的菜品
     */
    Dish update(DishDto dishDto);

    /**
     * 删除菜品
     * @param ids 菜品ID列表
     * @return 是否成功
     */
    boolean remove(List<Long> ids);

    /**
     * 根据分类ID查询菜品
     * @param categoryId 分类ID
     * @return 菜品列表
     */
    List<Dish> listByCategoryId(Long categoryId);

    /**
     * 根据分类ID查询在售菜品
     * @param categoryId 分类ID
     * @return 菜品列表
     */
    List<Dish> listByCategoryIdAndStatus(Long categoryId, Integer status);

    /**
     * 修改菜品状态（起售/停售）
     * @param ids 菜品ID列表
     * @param status 状态（0-停售, 1-起售）
     * @return 是否成功
     */
    boolean updateStatus(List<Long> ids, Integer status);

    /**
     * 分页查询菜品
     * @param categoryId 分类ID
     * @param name 菜品名称
     * @param status 状态
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<DishDto> page(Long categoryId, String name, Integer status, Pageable pageable);
}