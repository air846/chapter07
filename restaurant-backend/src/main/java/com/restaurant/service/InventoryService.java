package com.restaurant.service;

import com.restaurant.dto.InventoryDto;
import com.restaurant.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

/**
 * 库存服务接口
 */
public interface InventoryService {

    /**
     * 新增库存
     * @param inventoryDto 库存信息
     * @return 新增后的库存
     */
    Inventory save(InventoryDto inventoryDto);

    /**
     * 根据ID查询库存
     * @param id 库存ID
     * @return 库存信息
     */
    Inventory getById(Long id);

    /**
     * 更新库存
     * @param inventoryDto 库存信息
     * @return 更新后的库存
     */
    Inventory update(InventoryDto inventoryDto);

    /**
     * 删除库存
     * @param id 库存ID
     * @return 是否成功
     */
    boolean remove(Long id);

    /**
     * 入库操作
     * @param id 库存ID
     * @param quantity 入库数量
     * @return 是否成功
     */
    boolean stockIn(Long id, BigDecimal quantity);

    /**
     * 出库操作
     * @param id 库存ID
     * @param quantity 出库数量
     * @return 是否成功
     */
    boolean stockOut(Long id, BigDecimal quantity);

    /**
     * 获取库存预警列表
     * @param threshold 预警阈值
     * @return 预警列表
     */
    List<Inventory> getWarningList(BigDecimal threshold);

    /**
     * 分页查询库存
     * @param name 库存名称
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<Inventory> page(String name, Pageable pageable);
}
