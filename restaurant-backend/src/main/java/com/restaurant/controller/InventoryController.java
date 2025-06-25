package com.restaurant.controller;

import com.restaurant.common.Result;
import com.restaurant.dto.InventoryDto;
import com.restaurant.entity.Inventory;
import com.restaurant.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 库存控制器
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    /**
     * 分页查询库存列表
     */
    @GetMapping("/list")
    public Result<Page<Inventory>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name) {
        try {
            Pageable pageable = PageRequest.of(page - 1, pageSize);
            Page<Inventory> inventoryPage = inventoryService.page(name, pageable);
            return Result.success(inventoryPage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询库存预警列表
     */
    @GetMapping("/warning")
    public Result<List<Inventory>> warning(@RequestParam(defaultValue = "10") BigDecimal threshold) {
        try {
            List<Inventory> warningList = inventoryService.getWarningList(threshold);
            return Result.success(warningList);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据ID查询库存
     */
    @GetMapping("/{id}")
    public Result<Inventory> getById(@PathVariable Long id) {
        try {
            Inventory inventory = inventoryService.getById(id);
            return Result.success(inventory);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 新增库存
     */
    @PostMapping
    public Result<Inventory> save(@RequestBody InventoryDto inventoryDto) {
        try {
            Inventory savedInventory = inventoryService.save(inventoryDto);
            return Result.success(savedInventory);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新库存
     */
    @PutMapping
    public Result<Inventory> update(@RequestBody InventoryDto inventoryDto) {
        try {
            Inventory updatedInventory = inventoryService.update(inventoryDto);
            return Result.success(updatedInventory);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除库存
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        try {
            boolean success = inventoryService.remove(id);
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
     * 入库操作
     */
    @PutMapping("/in/{id}")
    public Result<String> stockIn(@PathVariable Long id, @RequestParam BigDecimal quantity) {
        try {
            boolean success = inventoryService.stockIn(id, quantity);
            if (success) {
                return Result.success("入库成功");
            } else {
                return Result.error("入库失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 出库操作
     */
    @PutMapping("/out/{id}")
    public Result<String> stockOut(@PathVariable Long id, @RequestParam BigDecimal quantity) {
        try {
            boolean success = inventoryService.stockOut(id, quantity);
            if (success) {
                return Result.success("出库成功");
            } else {
                return Result.error("出库失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
