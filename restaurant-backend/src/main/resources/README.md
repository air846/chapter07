# 餐饮管理系统数据库脚本

本目录包含餐饮管理系统的数据库初始化脚本。

## 文件说明

- `schema.sql`: 数据库表结构创建脚本
- `data.sql`: 初始数据插入脚本

## 数据库表结构

系统包含以下表：

1. `t_user`: 用户表，存储系统用户信息
2. `t_category`: 菜品分类表，存储菜品分类信息
3. `t_dish`: 菜品表，存储菜品信息
4. `t_inventory`: 库存表，存储库存信息
5. `t_order`: 订单表，存储订单信息
6. `t_order_detail`: 订单详情表，存储订单详情信息

## 初始数据

初始数据包括：

1. 三个用户账号：管理员(admin)、经理(manager)、服务员(staff)
2. 五个菜品分类：热菜、凉菜、主食、汤类、饮品
3. 十个菜品示例
4. 十个库存项目示例

## 使用说明

这些脚本会在应用启动时自动执行，无需手动运行。相关配置在`application.properties`文件中：

```properties
# SQL Initialization
spring.sql.init.mode=always
spring.sql.init.schema-locations=classpath:schema.sql
spring.sql.init.data-locations=classpath:data.sql
spring.sql.init.continue-on-error=true
```

如果需要禁用自动执行脚本，可以将`spring.sql.init.mode`设置为`never`。

## 注意事项

1. 数据库会通过JDBC URL中的`createDatabaseIfNotExist=true`参数自动创建，`schema.sql`脚本只负责创建表结构
2. 所有用户的初始密码都是加密后的字符串，对应的明文密码是`password`
3. 如果需要修改数据库结构，请同时更新实体类和SQL脚本
