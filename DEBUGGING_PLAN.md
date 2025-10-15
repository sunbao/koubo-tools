# 系统调试计划

## 1. 后端服务验证

### 1.1 API接口测试
- [x] GET /api/users/{id} - 获取用户信息
- [x] POST /api/users/login - 用户登录
- [x] POST /api/users/register - 用户注册
- [x] GET /api/hotspots - 获取所有热点
- [x] GET /api/hotspots/{id} - 获取特定热点
- [x] GET /api/scripts - 获取所有文案
- [x] GET /api/scripts/{id} - 获取特定文案
- [x] POST /api/scripts/generate - 生成文案
- [x] POST /api/teleprompter/speed - 计算最佳语速
- [x] POST /api/teleprompter/push - 推送到提词器
- [x] POST /api/teleprompter/control - 控制提词器
- [x] PUT /api/users/{id} - 更新用户信息
- [x] DELETE /api/scripts/{id} - 删除文案

### 1.2 功能验证
- [x] 用户认证流程
- [x] 热点数据获取
- [x] AI文案生成
- [x] 提词器语速调节
- [ ] 提词器文本滚动
- [x] 多端同步功能

## 2. 前端功能测试

### 2.1 页面功能测试
- [ ] 首页 (index)
  - [ ] 用户登录状态显示
  - [ ] 热点推荐展示
  - [ ] 最新文案展示
  - [ ] 快捷操作按钮

- [ ] 热点页面 (hotspots)
  - [ ] 热点数据列表展示
  - [ ] 平台筛选功能
  - [ ] 类型筛选功能
  - [ ] 生成文案功能

- [ ] 文案页面 (scripts)
  - [ ] 文案列表展示
  - [ ] 推送到提词器功能
  - [ ] 删除文案功能

- [ ] 提词器页面 (teleprompter)
  - [ ] 文案内容显示
  - [ ] 播放/暂停/停止控制
  - [ ] 语速调节滑块
  - [ ] 多端同步推送

- [ ] 个人中心 (profile)
  - [ ] 用户登录功能
  - [ ] 用户注册功能
  - [ ] 个人设置保存
  - [ ] 退出登录功能

### 2.2 交互测试
- [ ] 页面间导航
- [ ] 表单输入验证
- [ ] 网络请求处理
- [ ] 错误处理提示
- [ ] 加载状态显示

## 3. 集成测试

### 3.1 用户流程测试
- [ ] 新用户注册 → 登录 → 生成文案 → 提词器使用
- [ ] 已登录用户查看热点 → 生成文案 → 提词器使用
- [ ] 多端同步测试

### 3.2 性能测试
- [ ] 页面加载速度
- [ ] API响应时间
- [ ] 文本滚动流畅度

## 4. 调试工具和方法

### 4.1 后端调试
- 使用curl命令测试API接口
- 查看服务器日志输出
- 验证数据返回格式

### 4.2 前端调试
- 微信开发者工具控制台
- 网络面板监控请求
- Storage面板检查本地存储

## 5. 测试用例

### 5.1 API测试用例
```bash
# 获取所有热点
curl http://localhost:8083/api/hotspots

# 用户登录
curl -X POST http://localhost:8083/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password"}'

# 用户注册
curl -X POST http://localhost:8083/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"newuser","password":"password","broadcastType":"教育"}'

# 生成文案
curl -X POST "http://localhost:8083/api/scripts/generate?userId=1&hotspotId=1"

# 计算最佳语速
curl -X POST "http://localhost:8083/api/teleprompter/speed?scriptId=1"

# 推送到提词器
curl -X POST "http://localhost:8083/api/teleprompter/push?scriptId=1"

# 控制提词器
curl -X POST "http://localhost:8083/api/teleprompter/control?command=PLAY"

# 更新用户信息
curl -X PUT http://localhost:8083/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"accountStyle":"专业严谨","platformSettings":"快手"}'

# 删除文案
curl -X DELETE http://localhost:8083/api/scripts/2

# 获取特定文案
curl http://localhost:8083/api/scripts/1
```

### 5.2 WebSocket测试
1. 访问 http://localhost:8085/test_websocket.html
2. 点击"连接WebSocket"
3. 打开多个浏览器窗口测试多端同步
4. 发送消息验证广播功能
5. 测试提词器控制命令

### 5.3 前端测试步骤
1. 启动微信开发者工具
2. 导入项目
3. 配置后端服务地址
4. 逐个测试页面功能
5. 验证用户流程
6. 检查错误处理

## 6. 常见问题排查

### 6.1 网络连接问题
- 检查后端服务是否启动
- 验证API地址配置
- 检查防火墙设置

### 6.2 数据显示问题
- 检查API返回数据格式
- 验证前端数据绑定
- 查看控制台错误信息

### 6.3 功能异常问题
- 检查事件绑定
- 验证业务逻辑
- 查看相关依赖

## 7. 调试完成标准

- [x] 所有API接口正常响应
- [ ] 所有页面功能正常运行
- [ ] 用户流程完整可用
- [ ] 错误处理机制有效
- [ ] 性能满足基本要求