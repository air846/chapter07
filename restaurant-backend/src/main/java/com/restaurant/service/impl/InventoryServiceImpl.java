package com.restaurant.service.impl;

import com.restaurant.dto.InventoryDto;
import com.restaurant.entity.Inventory;
import com.restaurant.mapper.InventoryMapper;
import com.restaurant.service.InventoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 库存服务实现类
 */
@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryMapper inventoryMapper;

    @Override
    @Transactional
    public Inventory save(InventoryDto inventoryDto) {
        // 检查库存名称是否已存在
        if (inventoryMapper.existsByName(inventoryDto.getName())) {
            throw new RuntimeException("库存名称已存在");
        }
        
        Inventory inventory = new Inventory();
        BeanUtils.copyProperties(inventoryDto, inventory);
        
        return inventoryMapper.save(inventory);
    }

    @Override
    public Inventory getById(Long id) {
        return inventoryMapper.findById(id)
                .orElseThrow(() -> new RuntimeException("库存不存在"));
    }

    @Override
    @Transactional
    public Inventory update(InventoryDto inventoryDto) {
        Inventory inventory = inventoryMapper.findById(inventoryDto.getId())
                .orElseThrow(() -> new RuntimeException("库存不存在"));
        
        // 检查名称是否重复（排除自己）
        Inventory existingInventory = inventoryMapper.findByName(inventoryDto.getName());
        if (existingInventory != null && !existingInventory.getId().equals(inventoryDto.getId())) {
            throw new RuntimeException("库存名称已存在");
        }
        
        BeanUtils.copyProperties(inventoryDto, inventory);
        
        return inventoryMapper.save(inventory);
    }

    @Override
    @Transactional
    public boolean remove(Long id) {
        Inventory inventory = inventoryMapper.findById(id)
                .orElseThrow(() -> new RuntimeException("库存不存在"));
        
        inventoryMapper.delete(inventory);
        
        return true;
    }

    @Override
    @Transactional
    public boolean stockIn(Long id, BigDecimal quantity) {
        Inventory inventory = inventoryMapper.findById(id)
                .orElseThrow(() -> new RuntimeException("库存不存在"));
        
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("入库数量必须大于0");
        }
        
        inventory.setQuantity(inventory.getQuantity().add(quantity));
        inventoryMapper.save(inventory);
        
        return true;
    }

    @Override
    @Transactional
    public boolean stockOut(Long id, BigDecimal quantity) {
        Inventory inventory = inventoryMapper.findById(id)
                .orElseThrow(() -> new RuntimeException("库存不存在"));
        
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("出库数量必须大于0");
        }
        
        if (inventory.getQuantity().compareTo(quantity) < 0) {
            throw new RuntimeException("库存不足");
        }
        
        inventory.setQuantity(inventory.getQuantity().subtract(quantity));
        inventoryMapper.save(inventory);
        
        return true;
    }

    @Override
    public List<Inventory> getWarningList(BigDecimal threshold) {
        return inventoryMapper.findByQuantityLessThan(threshold);
    }

    @Override
    public Page<Inventory> page(String name, Pageable pageable) {
        Inventory probe = new Inventory();
        if (name != null) {
            probe.setName(name);
        }
        
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        
        Example<Inventory> example = Example.of(probe, matcher);
        
        return inventoryMapper.findAll(example, pageable);
    }
}
