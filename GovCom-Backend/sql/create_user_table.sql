-- ----------------------------
-- 1. 创建数据库
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
CREATE TABLE `user` (
                            id BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID（主键）',
                            userAccount VARCHAR(50) NOT NULL COMMENT '账号',
                            userPassword VARCHAR(100) NOT NULL COMMENT '密码',
                            userName VARCHAR(50) DEFAULT '' COMMENT '用户姓名',
                            Gender       TINYINT COMMENT '性别:0-男;1-女',
                            Phone VARCHAR(11) DEFAULT '' COMMENT '手机号',
                            userAvatar VARCHAR(255) DEFAULT '' COMMENT '头像URL',
                            userType TINYINT DEFAULT 1 COMMENT '用户类型：1-普通用户，2-政务人员，3-社区管理员，4-系统管理员',
                            status TINYINT DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
                            isDeleted    TINYINT  DEFAULT 0 COMMENT '逻辑删除标识:0-未删除;1-已删除',
                            createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            updateTime   DATETIME DEFAULT CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
                            PRIMARY KEY (id),
                            UNIQUE KEY `idx_userName` (userAccount)  -- 索引名随字段名用驼峰
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
