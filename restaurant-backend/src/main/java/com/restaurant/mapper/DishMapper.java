package com.restaurant.mapper;

import com.restaurant.entity.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜品数据访问接口
 */
@Repository
public interface DishMapper extends JpaRepository<Dish, Long> {
    
    /**
     * 根据分类ID查询菜品
     * @param categoryId 分类ID
     * @return 菜品列表
     */
    List<Dish> findByCategoryId(Long categoryId);
    
    /**
     * 根据分类ID和状态查询菜品
     * @param categoryId 分类ID
     * @param status 状态（0-停售, 1-起售）
     * @return 菜品列表
     */
    List<Dish> findByCategoryIdAndStatus(Long categoryId, Integer status);
    
    /**
     * 根据名称模糊查询菜品
     * @param name 菜品名称
     * @return 菜品列表
     */
    List<Dish> findByNameContaining(String name);
    
    /**
     * 分页查询菜品
     * @param categoryId 分类ID
     * @param name 菜品名称
     * @param status 状态
     * @param pageable 分页参数
     * @return 分页结果
     */
    @Query("SELECT d FROM Dish d WHERE (:categoryId IS NULL OR d.categoryId = :categoryId) " +
           "AND (:name IS NULL OR d.name LIKE CONCAT('%', :name, '%')) " +
           "AND (:status IS NULL OR d.status = :status)")
    Page<Dish> findDishPage(
            @Param("categoryId") Long categoryId,
            @Param("name") String name,
            @Param("status") Integer status,
            Pageable pageable);
}