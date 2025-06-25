package com.restaurant.mapper;

import com.restaurant.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 库存数据访问接口
 */
@Repository
public interface InventoryMapper extends JpaRepository<Inventory, Long> {
    
    /**
     * 根据名称查询库存
     * @param name 库存名称
     * @return 库存对象
     */
    Inventory findByName(String name);

    /**
     * 检查库存名称是否存在
     * @param name 库存名称
     * @return 是否存在
     */
    boolean existsByName(String name);
    
    /**
     * 根据名称模糊查询库存
     * @param name 库存名称
     * @return 库存列表
     */
    List<Inventory> findByNameContaining(String name);
    
    /**
     * 查询库存数量低于指定值的库存
     * @param threshold 阈值
     * @return 库存列表
     */
    List<Inventory> findByQuantityLessThan(BigDecimal threshold);
    
    /**
     * 分页查询库存
     * @param name 库存名称
     * @param minQuantity 最小数量
     * @param maxQuantity 最大数量
     * @param pageable 分页参数
     * @return 分页结果
     */
    @Query("SELECT i FROM Inventory i WHERE (:name IS NULL OR i.name LIKE CONCAT('%', :name, '%')) " +
           "AND (:minQuantity IS NULL OR i.quantity >= :minQuantity) " +
           "AND (:maxQuantity IS NULL OR i.quantity <= :maxQuantity)")
    Page<Inventory> findInventoryPage(
            @Param("name") String name,
            @Param("minQuantity") BigDecimal minQuantity,
            @Param("maxQuantity") BigDecimal maxQuantity,
            Pageable pageable);
}