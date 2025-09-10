# 餐饮管理系统

基于 Spring Boot + Vue.js 开发的全栈餐饮管理系统

## 🚀 技术栈

### 后端
- **Java 21** - 最新的LTS版本
- **Spring Boot 3.2.0** - 企业级Java框架
- **Spring Data JPA** - 数据访问层
- **Spring Security** - 安全框架
- **MySQL 8.0** - 关系型数据库
- **JWT认证** - 无状态身份验证

### 前端
- **Vue.js 3** - 渐进式JavaScript框架
- **Element Plus** - Vue 3 UI组件库
- **Vue Router** - 官方路由管理器
- **Pinia** - 状态管理
- **Axios** - HTTP客户端

## 📁 项目结构

```
chapter07/
├── restaurant-backend/     # 后端项目
│   ├── src/main/java/com/restaurant/
│   │   ├── controller/     # 控制层
│   │   ├── service/        # 服务层
│   │   ├── entity/         # 实体类
│   │   ├── mapper/         # 数据访问层
│   │   ├── dto/           # 数据传输对象
│   │   ├── config/        # 配置类
│   │   └── common/        # 公共组件
│   └── pom.xml            # Maven配置
├── restaurant-frontend/    # 前端项目
│   ├── src/
│   │   ├── views/         # 页面组件
│   │   ├── components/    # 公共组件
│   │   ├── api/          # API接口
│   │   ├── router/       # 路由配置
│   │   ├── store/        # 状态管理
│   │   └── utils/        # 工具函数
│   └── package.json       # 前端依赖配置
└── 餐饮管理系统实现文档.md  # 详细技术文档
```

## ✨ 功能模块

- 🔐 **用户认证与权限管理** - 多角色登录，基于JWT的安全认证
- 🍽️ **菜品分类与管理** - 菜品信息维护，分类管理，上下架控制
- 📋 **订单处理与跟踪** - 订单创建、状态流转、支付管理
- 📦 **库存管理** - 原材料库存监控，预警提醒
- 👥 **员工管理** - 员工信息维护，权限分配
- 📊 **数据统计与分析** - 销售报表，经营数据可视化

## 🛠️ 快速开始

### 环境要求

- **JDK 21+**
- **Node.js 18+**
- **MySQL 8.0+**
- **Maven 3.6+**

### 后端启动

```bash
cd restaurant-backend
mvn clean install
mvn spring-boot:run
```

### 前端启动

```bash
cd restaurant-frontend
npm install
npm run serve
```

## 🌐 访问地址

- **前端应用**: http://localhost:8080
- **后端API**: http://localhost:8081
- **API文档**: http://localhost:8081/swagger-ui.html

## 👤 默认账户

| 角色 | 用户名 | 密码 | 权限 |
|------|--------|------|------|
| 管理员 | admin | 123456 | 全部功能 |
| 经理 | manager | 123456 | 业务管理 |
| 员工 | staff | 123456 | 基础操作 |

## 🎯 系统特点

- ✅ **前后端分离** - 清晰的架构设计，便于维护和扩展
- ✅ **响应式设计** - 适配不同设备屏幕
- ✅ **权限控制** - 基于角色的细粒度权限管理
- ✅ **数据安全** - 密码加密存储，JWT令牌认证
- ✅ **用户体验** - 现代化UI设计，操作流畅
- ✅ **可扩展性** - 模块化设计，易于功能扩展

## 📖 详细文档

查看 [餐饮管理系统实现文档.md](./餐饮管理系统实现文档.md) 了解：
- 系统架构设计
- 数据库设计
- API接口规范
- 开发指南
- 部署说明

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 联系方式

如有问题或建议，欢迎提交 Issue 或联系开发团队。

---

⭐ 如果这个项目对您有帮助，请给个星星支持！