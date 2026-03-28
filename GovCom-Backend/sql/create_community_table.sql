-- 使用数据库
USE `pu_yu`;

-- =====================================================
-- 社区表 (community)
-- =====================================================
DROP TABLE IF EXISTS `community`;
CREATE TABLE `community` (
                             `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                             `communityName` VARCHAR(100) NOT NULL COMMENT '社区名称',
                             `communityCode` VARCHAR(50) COMMENT '社区编码',
                             `address` VARCHAR(200) COMMENT '社区地址',
                             `contactPhone` VARCHAR(20) COMMENT '联系电话',
                             `propertyCompany` VARCHAR(100) COMMENT '物业公司',
                             `intro` VARCHAR(500) COMMENT '社区简介',
                             `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
                             `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `updateTime` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             `isDeleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社区表';

-- 插入示例社区数据
INSERT INTO `community` (`communityName`, `communityCode`, `address`, `contactPhone`, `intro`)
VALUES ('渝中区大溪沟社区', 'YZ001', '重庆市渝中区大溪沟街道', '023-12345678', '位于渝中区核心地段，配套设施完善'),
       ('南岸区海棠溪社区', 'NA001', '重庆市南岸区海棠溪街道', '023-87654321', '滨江社区，环境优美'),
       ('江北区观音桥社区', 'JB001', '重庆市江北区观音桥商圈', '023-11223344', '商业繁华，交通便利');


-- =====================================================
-- 用户社区关联表 (user_community)
-- =====================================================
DROP TABLE IF EXISTS `user_community`;
CREATE TABLE `user_community` (
                                  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                  `userId` BIGINT NOT NULL COMMENT '用户ID',
                                  `communityId` INT NOT NULL COMMENT '社区ID',
                                  `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
                                  `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
                                  `updateTime` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  `isDeleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                                  UNIQUE KEY `uk_user_community` (`userId`, `communityId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户社区关联表';

-- 插入示例数据
INSERT INTO `user_community` (`userId`, `communityId`)
VALUES (2032016555332161537, 1),  -- 千郁郁加入大溪沟社区
       (2032016555332161537, 2);  -- 千郁郁加入海棠溪社区