-- 使用数据库
USE `pu_yu`;

-- =====================================================
-- 1. 社区帖子表 (community_post)
-- =====================================================
DROP TABLE IF EXISTS `community_post`;
CREATE TABLE `community_post` (
                                  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                  `userId` BIGINT NOT NULL COMMENT '发布者ID',
                                  `communityId` INT COMMENT '所属社区ID（可为空，表示全平台）',
                                  `title` VARCHAR(200) NOT NULL COMMENT '标题',
                                  `content` TEXT COMMENT '内容',
                                  `type` TINYINT COMMENT '类型：1-互助，2-二手，3-活动，4-闲聊',
                                  `images` TEXT COMMENT '图片（JSON数组）',
                                  `viewCount` INT DEFAULT 0 COMMENT '浏览次数',
                                  `likeCount` INT DEFAULT 0 COMMENT '点赞次数',
                                  `commentCount` INT DEFAULT 0 COMMENT '评论次数',
                                  `status` TINYINT DEFAULT 1 COMMENT '状态：0-删除，1-正常，2-置顶',
                                  `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `updateTime` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  `isDeleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                                  INDEX `idx_user` (`userId`),
                                  INDEX `idx_community` (`communityId`),
                                  INDEX `idx_type` (`type`),
                                  INDEX `idx_create` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社区帖子表';

-- =====================================================
-- 2. 社区评论表 (community_comment)
-- =====================================================
DROP TABLE IF EXISTS `community_comment`;
CREATE TABLE `community_comment` (
                                     `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                     `postId` INT NOT NULL COMMENT '帖子ID',
                                     `userId` BIGINT NOT NULL COMMENT '评论者ID',
                                     `parentId` INT DEFAULT 0 COMMENT '父评论ID（0表示一级评论）',
                                     `content` VARCHAR(500) NOT NULL COMMENT '评论内容',
                                     `likeCount` INT DEFAULT 0 COMMENT '点赞次数',
                                     `status` TINYINT DEFAULT 1 COMMENT '状态：0-删除，1-正常',
                                     `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `updateTime` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                     `isDeleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                                     INDEX `idx_post` (`postId`),
                                     INDEX `idx_user` (`userId`),
                                     INDEX `idx_parent` (`parentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社区评论表';

-- =====================================================
-- 3. 社区点赞表 (community_like)
-- =====================================================
DROP TABLE IF EXISTS `community_like`;
CREATE TABLE `community_like` (
                                  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                  `userId` BIGINT NOT NULL COMMENT '用户ID',
                                  `postId` INT COMMENT '帖子ID',
                                  `commentId` INT COMMENT '评论ID',
                                  `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `updateTime` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  `isDeleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                                  UNIQUE KEY `uk_user_post` (`userId`, `postId`),
                                  UNIQUE KEY `uk_user_comment` (`userId`, `commentId`),
                                  INDEX `idx_user` (`userId`),
                                  INDEX `idx_post` (`postId`),
                                  INDEX `idx_comment` (`commentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社区点赞表';

-- =====================================================
-- 4. 插入示例数据
-- =====================================================
-- 插入示例帖子
INSERT INTO `community_post` (`userId`, `communityId`, `title`, `content`, `type`, `viewCount`, `likeCount`, `commentCount`) VALUES
                                                                                                                                 (2032016555332161537, 101, '小区最近有一起打球的邻居吗？', '周末想找几个球友一起打篮球，有没有人组队？', 1, 120, 15, 5),
                                                                                                                                 (2032016555332161537, 101, '闲置婴儿车转让', '九成新，用了不到3个月，150元自提', 2, 80, 8, 3),
                                                                                                                                 (2032016555332161537, 102, '海棠溪社区周末亲子活动', '本周六上午9点在社区广场举办亲子运动会，欢迎报名', 3, 200, 25, 10),
                                                                                                                                 (2029515503802257409, 101, '小区停车位改造方案讨论', '大家觉得目前的停车位改造方案怎么样？', 4, 300, 30, 15);

-- 插入示例评论
INSERT INTO `community_comment` (`postId`, `userId`, `content`) VALUES
                                                                    (1, 2029515503802257409, '我也喜欢打球，周末可以一起'),
                                                                    (1, 2032016555332161537, '好啊，加个微信约时间'),
                                                                    (2, 2029515503802257409, '能看看实物照片吗？');

-- 插入示例点赞
INSERT INTO `community_like` (`userId`, `postId`) VALUES
                                                      (2029515503802257409, 1),
                                                      (2032016555332161537, 2);