# Gov-Com 政务社区服务一体化创新平台

## 项目简介

​	该平台是一个集政务服务、社区互动、便民生活于一体的创新服务平台。平台以“数据多跑路，群众少跑腿”为核心理念，为居民提供一站式在线服务体验。

### 主要功能

- **用户中心**：注册登录、个人信息管理、密码修改，查看我的政务申请，社区管理，社区服务申请
- **政务服务**：查看政务服务，在线申请政务服务
- **资讯中心**：政务资讯、个人社区公告、资讯搜索(有bug)
- **社区互动**：邻里交流、帖子发布、评论点赞
- **便民生活**：社区证明、社区帮助、投诉建议
- **工单处理**：根据用户权限，工作人员分别  政务 / 社区工单

## 技术栈

### 后端（application.yml）
- Java 8
- Spring Boot 2.7.x
- MyBatis-Plus 3.5.x
- Hutool 5.8.x
- MySQL 8.0

### 前端（package.json）
- Vue 3
- Ant Design Vue 4.x
- Vite 6.x
- TypeScript
- Day.js

## 项目结构

GovCom-Backend/ 	# 后端项目
├── src/main/java/
│ └── com/gjj/govcombackend/
│ ├── annotation/      
│ ├── aop/ 	            # 用户校验切面
│ ├── controller/ 	  # 控制器层
│ ├── controller/ 	 # 控制器层
│ ├── service/ 	     # 服务层
│ ├── mapper/ 	   # 数据访问层
│ ├── model/ 	     # 实体类、DTO、VO
│ ├── config/	      # 配置类
│ └── common/	 # 公共类
├── src/main/resources/
│ └── application.yml    # 配置文件
└── sql/ 			# 数据库脚本

yu-picture-frontend/ # 前端项目
├── src/
│ ├── api/                       # umi-openapi 生成的API接口
│ ├── assets/                 # 静态资源
│ ├── components/     # 公共组件
│ ├── layouts/            # 布局组件
│ ├── pages/            # 页面组件
│ ├── router/          # 路由配置
│ ├── stores/         # 状态管理
│ └── utils/          # 工具函数
├── index.html
└── package.json

## 快速开始

### 环境要求

- JDK 8+
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+

### 后端启动

1. **执行SQL脚本，创建数据库**
   依次执行 `sql/` 目录下的脚本：

- `create_user_table.sql` 
- `create_gov_service_table.sql`
- `create_community_table.sql`
- `create_info_table.sql`
- `create_community_interaction.sql`
- `create_com_work_order_table.sql`

1. **修改配置文件**
   编辑 `src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/gov_com
    username: root
    password: your_password
```

1. **启动项目**  GovComBackendApplication

### 前端启动

1. **安装依赖**

```shell
cd yu-picture-frontend
npm install
```

1. **启动开发服务器**

```shell
npm run dev
```

1. **访问系统**
   打开浏览器访问 `http://localhost:5173`

## 角色权限

| 角色       | userType       | 可访问模块                                       |
| :--------- | :------------- | :----------------------------------------------- |
| 普通用户   | 1              | 政务服务、资讯中心、社区互动、便民生活、个人中心 |
| 政务人员   | 2              | 政务工单处理、个人中心                           |
| 社区人员   | 3              | 社区工单处理、个人中心                           |
| 系统管理员 | 4 （我还没做） | 全部模块                                         |

## 数据库表清单

| 模块     | 表名                        | 说明           |
| :------- |:--------------------------| :------------- |
| 用户中心 | `user`                    | 用户表         |
| 用户中心 | `user_community`          | 用户社区关联表 |
| 政务服务 | `gov_service_category`    | 服务分类表     |
| 政务服务 | `gov_service_item`        | 服务事项表     |
| 政务服务 | `gov_service_application` | 政务服务申请表 |
| 资讯中心 | `info_category`           | 资讯分类表     |
| 资讯中心 | `info_article`            | 资讯内容表     |
| 社区互动 | `community`               | 社区表         |
| 社区互动 | `community_post`          | 社区帖子表     |
| 社区互动 | `community_comment`       | 社区评论表     |
| 社区互动 | `community_like`          | 社区点赞表     |
| 便民生活 | `com_work_order`          | 工单表         |
| 便民生活 | `com_work_order_log`          | 工单处理记录表 |

## API文档

启动后端后，访问 `http://localhost:8101/swagger-ui.html` 查看接口文档。
