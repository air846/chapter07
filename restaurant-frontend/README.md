# 餐饮管理系统前端

基于 Vue 3 + Element Plus 开发的餐饮管理系统前端项目。

## 技术栈

- **Vue 3** - 渐进式 JavaScript 框架
- **Vue Router 4** - 官方路由管理器
- **Pinia** - 状态管理库
- **Element Plus** - Vue 3 组件库
- **Axios** - HTTP 客户端
- **Sass** - CSS 预处理器

## 功能特性

### 🎨 界面设计
- 现代化的 UI 设计
- 响应式布局，支持移动端
- 文本动画效果（参考 reactbits.dev）
- 全屏显示模式

### 🔐 用户认证
- JWT Token 认证
- 自动登录状态检查
- 路由权限控制

### 📊 核心功能
- **控制台** - 数据概览和快捷操作
- **菜品管理** - 菜品分类和菜品列表管理
- **订单管理** - 订单查看、状态管理和详情查看
- **库存管理** - 库存查看、入库出库操作和预警提醒
- **员工管理** - 员工信息管理（开发中）
- **统计分析** - 数据统计和图表展示

### ✨ 特色功能
- 文本分割动画组件
- 实时时间显示
- 库存预警提醒
- 订单状态流转
- 图片上传预览

## 项目结构

```
restaurant-frontend/
├── public/                 # 静态资源
├── src/
│   ├── api/               # API 接口
│   ├── assets/            # 资源文件
│   │   └── styles/        # 样式文件
│   ├── components/        # 公共组件
│   │   └── common/        # 通用组件
│   ├── router/            # 路由配置
│   ├── store/             # 状态管理
│   ├── utils/             # 工具函数
│   ├── views/             # 页面组件
│   │   ├── dashboard/     # 控制台
│   │   ├── dish/          # 菜品管理
│   │   ├── employee/      # 员工管理
│   │   ├── inventory/     # 库存管理
│   │   ├── layout/        # 布局组件
│   │   ├── login/         # 登录页面
│   │   ├── order/         # 订单管理
│   │   └── statistics/    # 统计分析
│   ├── App.vue            # 根组件
│   └── main.js            # 入口文件
├── package.json           # 项目配置
├── vue.config.js          # Vue CLI 配置
└── README.md              # 项目说明
```

## 安装和运行

### 环境要求
- Node.js 18+
- npm 或 yarn

### 安装依赖
```bash
cd restaurant-frontend
npm install
```

### 开发环境运行
```bash
npm run dev
```

### 生产环境构建
```bash
npm run build
```

### 代码检查
```bash
npm run lint
```

## 配置说明

### 后端 API 配置
在 `vue.config.js` 中配置后端 API 代理：

```javascript
devServer: {
  proxy: {
    '/api': {
      target: 'http://localhost:8081',  // 后端服务地址
      changeOrigin: true
    }
  }
}
```

### 环境变量
可以创建 `.env.local` 文件配置环境变量：

```
VUE_APP_BASE_API=/api
```

## 页面说明

### 登录页面
- 用户名/密码登录
- 文本动画效果
- 响应式设计
- 默认账号：admin / 123456

### 控制台
- 数据统计卡片
- 实时时间显示
- 快捷操作入口
- 最近订单列表

### 菜品管理
- 菜品分类管理
- 菜品列表管理
- 图片上传功能
- 状态切换

### 订单管理
- 订单列表查看
- 订单状态管理
- 订单详情查看
- 接单/拒绝操作

### 库存管理
- 库存列表查看
- 入库/出库操作
- 库存预警提醒
- 库存统计

### 统计分析
- 数据概览
- 图表展示（占位符）
- 热门菜品排行
- 支付方式统计

## 组件说明

### SplitText 组件
文本分割动画组件，参考 reactbits.dev 的设计理念：

```vue
<SplitText 
  text="餐饮管理系统" 
  :delay="100"
  :auto-play="true"
  @animation-complete="onComplete"
/>
```

参数：
- `text`: 要显示的文本
- `delay`: 字符间动画延迟（毫秒）
- `auto-play`: 是否自动播放
- `trigger`: 手动触发动画

## 样式规范

### SCSS 变量
在 `src/assets/styles/variables.scss` 中定义了全局样式变量：

- 颜色变量
- 尺寸变量
- 字体变量
- 阴影变量

### 响应式设计
使用媒体查询实现响应式布局：

```scss
@media (max-width: 768px) {
  // 移动端样式
}
```

## API 接口

### 用户相关
- `POST /api/user/login` - 用户登录
- `POST /api/user/logout` - 用户登出
- `GET /api/user/info` - 获取用户信息

### 菜品相关
- `GET /api/dish/list` - 获取菜品列表
- `POST /api/dish` - 添加菜品
- `PUT /api/dish` - 更新菜品
- `DELETE /api/dish/:id` - 删除菜品

### 订单相关
- `GET /api/order/list` - 获取订单列表
- `GET /api/order/:id` - 获取订单详情
- `PUT /api/order/:id/confirm` - 确认订单
- `PUT /api/order/:id/complete` - 完成订单

### 库存相关
- `GET /api/inventory/list` - 获取库存列表
- `POST /api/inventory/stock-in` - 入库操作
- `POST /api/inventory/stock-out` - 出库操作
- `GET /api/inventory/warning` - 获取库存预警

## 开发注意事项

1. **代码规范**：使用 ESLint 进行代码检查
2. **组件命名**：使用 PascalCase 命名组件
3. **样式隔离**：使用 scoped 样式避免污染
4. **响应式**：确保在移动端正常显示
5. **错误处理**：统一的错误提示和处理

## 浏览器支持

- Chrome >= 87
- Firefox >= 78
- Safari >= 14
- Edge >= 88

## 许可证

MIT License
