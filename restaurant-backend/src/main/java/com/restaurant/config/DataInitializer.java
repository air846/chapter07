package com.restaurant.config;

import com.restaurant.entity.Category;
import com.restaurant.entity.Dish;
import com.restaurant.entity.Inventory;
import com.restaurant.entity.User;
import com.restaurant.mapper.CategoryMapper;
import com.restaurant.mapper.DishMapper;
import com.restaurant.mapper.InventoryMapper;
import com.restaurant.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 数据初始化器
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initUsers();
        initCategories();
        initDishes();
        initInventory();
    }

    private void initUsers() {
        long userCount = userMapper.count();
        System.out.println("当前用户数量: " + userCount);
        if (userCount == 0) {
            // 创建管理员用户
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setName("系统管理员");
            admin.setPhone("13800138000");
            admin.setEmail("admin@restaurant.com");
            admin.setRole("ADMIN");
            admin.setStatus(1);
            userMapper.save(admin);

            // 创建经理用户
            User manager = new User();
            manager.setUsername("manager");
            manager.setPassword(passwordEncoder.encode("password"));
            manager.setName("餐厅经理");
            manager.setPhone("13800138001");
            manager.setEmail("manager@restaurant.com");
            manager.setRole("MANAGER");
            manager.setStatus(1);
            userMapper.save(manager);

            // 创建员工用户
            User staff = new User();
            staff.setUsername("staff");
            staff.setPassword(passwordEncoder.encode("password"));
            staff.setName("服务员");
            staff.setPhone("13800138002");
            staff.setEmail("staff@restaurant.com");
            staff.setRole("STAFF");
            staff.setStatus(1);
            userMapper.save(staff);

            System.out.println("初始用户数据创建完成");
        }
    }

    private void initCategories() {
        long categoryCount = categoryMapper.count();
        System.out.println("当前分类数量: " + categoryCount);
        if (categoryCount == 0) {
            String[] categoryNames = {"热菜", "凉菜", "主食", "汤类", "饮品"};
            for (int i = 0; i < categoryNames.length; i++) {
                Category category = new Category();
                category.setName(categoryNames[i]);
                category.setSort(i + 1);
                categoryMapper.save(category);
            }
            System.out.println("初始分类数据创建完成");
        }
    }

    private void initDishes() {
        long dishCount = dishMapper.count();
        System.out.println("当前菜品数量: " + dishCount);
        if (dishCount == 0) {
            // 获取分类
            Category hotDish = categoryMapper.findByName("热菜");
            Category coldDish = categoryMapper.findByName("凉菜");
            Category staple = categoryMapper.findByName("主食");
            Category soup = categoryMapper.findByName("汤类");
            Category drink = categoryMapper.findByName("饮品");

            // 创建菜品
            createDish("宫保鸡丁", hotDish.getId(), new BigDecimal("38.00"), "宫保鸡丁，是一道闻名中外的特色传统名菜。");
            createDish("鱼香肉丝", hotDish.getId(), new BigDecimal("36.00"), "鱼香肉丝是一道经典川菜。");
            createDish("凉拌黄瓜", coldDish.getId(), new BigDecimal("18.00"), "清爽可口的凉拌黄瓜。");
            createDish("蒜泥白肉", coldDish.getId(), new BigDecimal("32.00"), "蒜泥白肉是一道四川传统名菜。");
            createDish("米饭", staple.getId(), new BigDecimal("3.00"), "香喷喷的米饭。");
            createDish("馒头", staple.getId(), new BigDecimal("2.00"), "松软可口的馒头。");
            createDish("紫菜蛋花汤", soup.getId(), new BigDecimal("15.00"), "营养丰富的紫菜蛋花汤。");
            createDish("酸辣汤", soup.getId(), new BigDecimal("18.00"), "开胃的酸辣汤。");
            createDish("可乐", drink.getId(), new BigDecimal("6.00"), "冰镇可乐。");
            createDish("雪碧", drink.getId(), new BigDecimal("6.00"), "冰镇雪碧。");

            System.out.println("初始菜品数据创建完成");
        }
    }

    private void createDish(String name, Long categoryId, BigDecimal price, String description) {
        Dish dish = new Dish();
        dish.setName(name);
        dish.setCategoryId(categoryId);
        dish.setPrice(price);
        dish.setDescription(description);
        dish.setStatus(1);
        dishMapper.save(dish);
    }

    private void initInventory() {
        long inventoryCount = inventoryMapper.count();
        System.out.println("当前库存数量: " + inventoryCount);
        if (inventoryCount == 0) {
            String[][] inventoryData = {
                {"鸡肉", "50.00", "斤"},
                {"猪肉", "80.00", "斤"},
                {"黄瓜", "30.00", "斤"},
                {"大蒜", "10.00", "斤"},
                {"米", "100.00", "斤"},
                {"面粉", "80.00", "斤"},
                {"紫菜", "5.00", "斤"},
                {"鸡蛋", "100.00", "个"},
                {"可乐", "50.00", "瓶"},
                {"雪碧", "50.00", "瓶"}
            };

            for (String[] data : inventoryData) {
                Inventory inventory = new Inventory();
                inventory.setName(data[0]);
                inventory.setQuantity(new BigDecimal(data[1]));
                inventory.setUnit(data[2]);
                inventoryMapper.save(inventory);
            }

            System.out.println("初始库存数据创建完成");
        }
    }
}
