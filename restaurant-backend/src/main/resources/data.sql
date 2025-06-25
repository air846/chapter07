
-- 插入初始用户数据
INSERT IGNORE INTO t_user (username, password, name, phone, email, role, status, create_time, update_time)
VALUES
('admin', '$2a$10$agm1klL.rxbyeVA1qsoVle9IIedJUrf4sHfPIP37.IZQ3J6FxTme6', '系统管理员', '13800138000', 'admin@restaurant.com', 'ADMIN', 1, NOW(), NOW()),
('manager', '$2a$10$agm1klL.rxbyeVA1qsoVle9IIedJUrf4sHfPIP37.IZQ3J6FxTme6', '餐厅经理', '13800138001', 'manager@restaurant.com', 'MANAGER', 1, NOW(), NOW()),
('staff', '$2a$10$agm1klL.rxbyeVA1qsoVle9IIedJUrf4sHfPIP37.IZQ3J6FxTme6', '服务员', '13800138002', 'staff@restaurant.com', 'STAFF', 1, NOW(), NOW());

-- 插入菜品分类数据
INSERT IGNORE INTO t_category (name, sort, create_time, update_time)
VALUES
('热菜', 1, NOW(), NOW()),
('凉菜', 2, NOW(), NOW()),
('主食', 3, NOW(), NOW()),
('汤类', 4, NOW(), NOW()),
('饮品', 5, NOW(), NOW());

-- 插入菜品数据
INSERT IGNORE INTO t_dish (name, category_id, price, image, description, status, create_time, update_time)
VALUES
('宫保鸡丁', 1, 38.00, 'static/images/gongbaojiding.jpg', '宫保鸡丁，是一道闻名中外的特色传统名菜。', 1, NOW(), NOW()),
('鱼香肉丝', 1, 36.00, 'static/images/yuxiangrousi.jpg', '鱼香肉丝是一道经典川菜。', 1, NOW(), NOW()),
('凉拌黄瓜', 2, 18.00, 'static/images/liangbanhuanggua.jpg', '清爽可口的凉拌黄瓜。', 1, NOW(), NOW()),
('蒜泥白肉', 2, 32.00, 'static/images/suannibarou.jpg', '蒜泥白肉是一道四川传统名菜。', 1, NOW(), NOW()),
('米饭', 3, 3.00, 'static/images/rice.jpg', '香喷喷的米饭。', 1, NOW(), NOW()),
('馒头', 3, 2.00, 'static/images/mantou.jpg', '松软可口的馒头。', 1, NOW(), NOW()),
('紫菜蛋花汤', 4, 15.00, 'static/images/zicaidanhuatang.jpg', '营养丰富的紫菜蛋花汤。', 1, NOW(), NOW()),
('酸辣汤', 4, 18.00, 'static/images/suanlatang.jpg', '开胃的酸辣汤。', 1, NOW(), NOW()),
('可乐', 5, 6.00, 'static/images/cola.jpg', '冰镇可乐。', 1, NOW(), NOW()),
('雪碧', 5, 6.00, 'static/images/sprite.jpg', '冰镇雪碧。', 1, NOW(), NOW());

-- 插入库存数据
INSERT IGNORE INTO t_inventory (name, quantity, unit, create_time, update_time)
VALUES
('鸡肉', 50.00, '斤', NOW(), NOW()),
('猪肉', 80.00, '斤', NOW(), NOW()),
('黄瓜', 30.00, '斤', NOW(), NOW()),
('大蒜', 10.00, '斤', NOW(), NOW()),
('米', 100.00, '斤', NOW(), NOW()),
('面粉', 80.00, '斤', NOW(), NOW()),
('紫菜', 5.00, '斤', NOW(), NOW()),
('鸡蛋', 100.00, '个', NOW(), NOW()),
('可乐', 50.00, '瓶', NOW(), NOW()),
('雪碧', 50.00, '瓶', NOW(), NOW());

-- 插入订单数据
INSERT IGNORE INTO t_order (number, status, user_id, order_time, pay_method, pay_status, amount, remark, create_time, update_time)
VALUES
('202506251001', 5, 1, '2025-06-25 12:30:00', 1, 1, 74.00, '不要辣', '2025-06-25 12:30:00', '2025-06-25 13:30:00'),
('202506251002', 5, 2, '2025-06-25 13:15:00', 2, 1, 56.00, '多放醋', '2025-06-25 13:15:00', '2025-06-25 14:15:00'),
('202506251003', 3, 3, '2025-06-25 18:45:00', 1, 1, 89.00, '', '2025-06-25 18:45:00', '2025-06-25 18:45:00'),
('202506251004', 2, 1, '2025-06-25 19:20:00', 1, 0, 45.00, '少盐', '2025-06-25 19:20:00', '2025-06-25 19:20:00'),
('202506251005', 1, 2, '2025-06-25 19:50:00', 1, 0, 32.00, '', '2025-06-25 19:50:00', '2025-06-25 19:50:00');

-- 插入订单详情数据
INSERT IGNORE INTO t_order_detail (order_id, dish_id, dish_name, price, number, amount, create_time)
VALUES
-- 订单1的详情
(1, 1, '宫保鸡丁', 38.00, 1, 38.00, '2025-06-25 12:30:00'),
(1, 2, '鱼香肉丝', 36.00, 1, 36.00, '2025-06-25 12:30:00'),
-- 订单2的详情
(2, 3, '凉拌黄瓜', 18.00, 1, 18.00, '2025-06-25 13:15:00'),
(2, 4, '蒜泥白肉', 32.00, 1, 32.00, '2025-06-25 13:15:00'),
(2, 9, '可乐', 6.00, 1, 6.00, '2025-06-25 13:15:00'),
-- 订单3的详情
(3, 1, '宫保鸡丁', 38.00, 2, 76.00, '2025-06-25 18:45:00'),
(3, 5, '米饭', 3.00, 2, 6.00, '2025-06-25 18:45:00'),
(3, 10, '雪碧', 6.00, 1, 6.00, '2025-06-25 18:45:00'),
-- 订单4的详情
(4, 3, '凉拌黄瓜', 18.00, 1, 18.00, '2025-06-25 19:20:00'),
(4, 7, '紫菜蛋花汤', 15.00, 1, 15.00, '2025-06-25 19:20:00'),
(4, 9, '可乐', 6.00, 2, 12.00, '2025-06-25 19:20:00'),
-- 订单5的详情
(5, 4, '蒜泥白肉', 32.00, 1, 32.00, '2025-06-25 19:50:00');
