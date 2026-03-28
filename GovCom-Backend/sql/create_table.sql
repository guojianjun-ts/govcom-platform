-- ----------------------------
-- 1. 创建数据库（保留下划线命名）
-- ----------------------------
DROP DATABASE IF EXISTS `pu_yu`;
CREATE DATABASE `pu_yu`
    DEFAULT CHARACTER SET utf8mb4  -- 支持emoji和所有中文
    DEFAULT COLLATE utf8mb4_unicode_ci;  -- 通用排序规则，适配中文

-- 切换到pu_yu数据库
USE `pu_yu`;

-- ----------------------------
-- 2. 用户核心表（表名下划线，字段名驼峰）
-- ----------------------------
DROP TABLE IF EXISTS user;
CREATE TABLE `sys_user` (
                            `userId` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID（主键）',
                            `userName` VARCHAR(50) NOT NULL COMMENT '用户名/手机号（登录用）',
                            `password` VARCHAR(100) NOT NULL COMMENT '密码（BCrypt加密存储）',
                            `nickName` VARCHAR(50) DEFAULT '' COMMENT '用户昵称',
                            `realName` VARCHAR(20) DEFAULT '' COMMENT '真实姓名',
                            `phone` VARCHAR(11) DEFAULT '' COMMENT '手机号（冗余，和userName一致）',
                            `avatar` VARCHAR(255) DEFAULT '' COMMENT '头像URL',
                            `userType` TINYINT DEFAULT 1 COMMENT '用户类型：1-普通用户，2-政务人员，3-社区管理员，4-系统管理员',
                            `status` TINYINT DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
                            `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            PRIMARY KEY (`userId`),
                            UNIQUE KEY `idx_userName` (`userName`)  -- 索引名随字段名用驼峰
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表（核心）';

-- ----------------------------
-- 3. 资讯服务模块
-- ----------------------------
DROP TABLE IF EXISTS `news_info`;
CREATE TABLE `news_info` (
                             `newsId` BIGINT NOT NULL AUTO_INCREMENT COMMENT '资讯ID',
                             `title` VARCHAR(200) NOT NULL COMMENT '资讯标题',
                             `content` TEXT NOT NULL COMMENT '资讯内容',
                             `coverUrl` VARCHAR(255) DEFAULT '' COMMENT '封面图URL',
                             `authorId` BIGINT NOT NULL COMMENT '发布人ID（关联sys_user.userId）',
                             `category` VARCHAR(50) DEFAULT '' COMMENT '资讯分类：政策/民生/社区',
                             `publishTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
                             `status` TINYINT DEFAULT 1 COMMENT '状态：1-发布，0-草稿',
                             PRIMARY KEY (`newsId`),
                             KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资讯表';

-- ----------------------------
-- 4. 政务服务模块
-- ----------------------------
DROP TABLE IF EXISTS `gov_service`;
CREATE TABLE `gov_service` (
                               `serviceId` BIGINT NOT NULL AUTO_INCREMENT COMMENT '政务事项ID',
                               `serviceName` VARCHAR(100) NOT NULL COMMENT '事项名称（如社保办理、医保报销）',
                               `serviceDesc` TEXT DEFAULT '' COMMENT '事项说明',
                               `handleDept` VARCHAR(100) DEFAULT '' COMMENT '办理部门',
                               `handleAddr` VARCHAR(255) DEFAULT '' COMMENT '办理地址',
                               `handleTime` VARCHAR(100) DEFAULT '' COMMENT '办理时间（如工作日9:00-17:00）',
                               `status` TINYINT DEFAULT 1 COMMENT '状态：1-有效，0-失效',
                               PRIMARY KEY (`serviceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='政务服务事项表';

DROP TABLE IF EXISTS `gov_apply`;
CREATE TABLE `gov_apply` (
                             `applyId` BIGINT NOT NULL AUTO_INCREMENT COMMENT '申请ID',
                             `userId` BIGINT NOT NULL COMMENT '申请人ID',
                             `serviceId` BIGINT NOT NULL COMMENT '申请事项ID',
                             `applyContent` VARCHAR(500) DEFAULT '' COMMENT '申请内容/备注',
                             `applyTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
                             `applyStatus` TINYINT DEFAULT 0 COMMENT '状态：0-待审核，1-审核通过，2-审核驳回',
                             `auditRemark` VARCHAR(200) DEFAULT '' COMMENT '审核备注',
                             PRIMARY KEY (`applyId`),
                             KEY `idx_userService` (`userId`,`serviceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='政务事项申请表';

-- ----------------------------
-- 5. 社区互动模块
-- ----------------------------
DROP TABLE IF EXISTS `community_post`;
CREATE TABLE `community_post` (
                                  `postId` BIGINT NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
                                  `userId` BIGINT NOT NULL COMMENT '发帖人ID',
                                  `title` VARCHAR(200) NOT NULL COMMENT '帖子标题',
                                  `content` TEXT NOT NULL COMMENT '帖子内容',
                                  `coverUrl` VARCHAR(255) DEFAULT '' COMMENT '封面图URL',
                                  `communityName` VARCHAR(100) DEFAULT '' COMMENT '所属社区（如XX小区）',
                                  `publishTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
                                  `likeNum` INT DEFAULT 0 COMMENT '点赞数',
                                  `commentNum` INT DEFAULT 0 COMMENT '评论数',
                                  PRIMARY KEY (`postId`),
                                  KEY `idx_userCommunity` (`userId`,`communityName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社区帖子表';

DROP TABLE IF EXISTS `post_comment`;
CREATE TABLE `post_comment` (
                                `commentId` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评论ID',
                                `postId` BIGINT NOT NULL COMMENT '帖子ID',
                                `userId` BIGINT NOT NULL COMMENT '评论人ID',
                                `content` VARCHAR(500) NOT NULL COMMENT '评论内容',
                                `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
                                PRIMARY KEY (`commentId`),
                                KEY `idx_postId` (`postId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子评论表';

-- ----------------------------
-- 6. 工单处理模块（核心）
-- ----------------------------
DROP TABLE IF EXISTS `work_order`;
CREATE TABLE `work_order` (
                              `orderId` BIGINT NOT NULL AUTO_INCREMENT COMMENT '工单ID',
                              `orderNo` VARCHAR(30) NOT NULL COMMENT '工单编号（如YW20260305001）',
                              `userId` BIGINT NOT NULL COMMENT '提交人ID',
                              `title` VARCHAR(200) NOT NULL COMMENT '工单标题',
                              `content` TEXT NOT NULL COMMENT '工单内容',
                              `orderType` VARCHAR(50) DEFAULT '' COMMENT '工单类型：咨询/投诉/建议',
                              `status` TINYINT DEFAULT 0 COMMENT '状态：0-待处理，1-处理中，2-已完结，3-已驳回',
                              `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `handleContent` TEXT DEFAULT '' COMMENT '处理结果',
                              `handleTime` DATETIME DEFAULT NULL COMMENT '处理完成时间',
                              PRIMARY KEY (`orderId`),
                              UNIQUE KEY `idx_orderNo` (`orderNo`),
                              KEY `idx_userStatus` (`userId`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单表';

-- ----------------------------
-- 7. 便民生活模块
-- ----------------------------
DROP TABLE IF EXISTS `life_service`;
CREATE TABLE `life_service` (
                                `serviceId` BIGINT NOT NULL AUTO_INCREMENT COMMENT '便民服务ID',
                                `serviceName` VARCHAR(100) NOT NULL COMMENT '服务名称（如家政、维修）',
                                `contact` VARCHAR(50) DEFAULT '' COMMENT '联系方式',
                                `address` VARCHAR(255) DEFAULT '' COMMENT '服务地址',
                                `serviceDesc` VARCHAR(500) DEFAULT '' COMMENT '服务说明',
                                PRIMARY KEY (`serviceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='便民服务表';

-- ----------------------------
-- 8. 系统基础表
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
                              `configId` BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置ID',
                              `configKey` VARCHAR(50) NOT NULL COMMENT '配置键（如siteName）',
                              `configValue` VARCHAR(255) NOT NULL COMMENT '配置值（如普渝惠生）',
                              PRIMARY KEY (`configId`),
                              UNIQUE KEY `idx_configKey` (`configKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- ----------------------------
-- 可选：插入初始化数据（方便测试）
-- ----------------------------
-- 1. 插入系统管理员用户（密码：123456，BCrypt加密）
INSERT INTO user (`userName`, `password`, `nickName`, `userType`, `status`)
VALUES ('admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '系统管理员', 4, 1);

-- 2. 插入系统配置（网站名称）
INSERT INTO `sys_config` (`configKey`, `configValue`)
VALUES ('siteName', '普渝惠生');

-- 验证语句（可选执行）
-- SELECT * FROM sys_user;
-- SELECT * FROM sys_config;