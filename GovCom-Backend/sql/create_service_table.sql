-- 使用数据库
USE `pu_yu`;

-- =====================================================
-- 1. 服务分类表 (service_category)
-- =====================================================
DROP TABLE IF EXISTS `service_category`;
CREATE TABLE `service_category` (
                                    `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                    `categoryName` VARCHAR(50) NOT NULL COMMENT '分类名称（如：医疗保障、社会保障）',
                                    `categoryCode` VARCHAR(50) COMMENT '分类编码（如：medical、social）',
                                    `icon` VARCHAR(50) COMMENT '图标名称（如：fa-heartbeat）',
                                    `sortOrder` INT DEFAULT 0 COMMENT '排序顺序',
                                    `status` TINYINT DEFAULT 1 COMMENT '状态：0-隐藏，1-显示',
                                    `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `updateTime` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    `isDeleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务分类表';

-- =====================================================
-- 2. 服务事项表 (service_item)
-- =====================================================
DROP TABLE IF EXISTS `service_item`;
CREATE TABLE `service_item` (
                                `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                `categoryId` INT NOT NULL COMMENT '所属分类ID',
                                `serviceName` VARCHAR(100) NOT NULL COMMENT '服务名称（如：生育保险生育津贴申领）',
                                `serviceCode` VARCHAR(50) COMMENT '服务编码',
                                `briefDesc` VARCHAR(200) COMMENT '简短描述',
                                `icon` VARCHAR(50) COMMENT '图标名称',
                                `materialList` TEXT COMMENT '办理材料（JSON数组格式，如：["身份证","出生证明","医院单据"]）',
                                `processDesc` TEXT COMMENT '办理流程描述',
                                `baseInfo` TEXT COMMENT '基本信息（办理时限、费用等，JSON格式）',
                                `viewCount` INT DEFAULT 0 COMMENT '浏览次数',
                                `applyCount` INT DEFAULT 0 COMMENT '申请次数',
                                `status` TINYINT DEFAULT 1 COMMENT '状态：0-下架，1-上架',
                                `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `updateTime` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                `isDeleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务事项表';

-- =====================================================
-- 3. 申请记录表 (service_application)
-- =====================================================
DROP TABLE IF EXISTS `service_application`;
CREATE TABLE `service_application` (
                                       `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                       `applicationNo` VARCHAR(50) COMMENT '申请单号',
                                       `userId` BIGINT NOT NULL COMMENT '用户ID',
                                       `serviceId` INT NOT NULL COMMENT '服务ID',
                                       `region` VARCHAR(50) DEFAULT '重庆市' COMMENT '地区（如：重庆市南岸区）',
                                       `applicantName` VARCHAR(50) COMMENT '申请人姓名',
                                       `applicantPhone` VARCHAR(20) COMMENT '联系电话',
                                       `status` TINYINT DEFAULT 1 COMMENT '状态：1-已提交，2-审核中，3-已完成，4-已驳回',
                                       `submitTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
                                       `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `updateTime` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                       `isDeleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务申请表';

-- =====================================================
-- 4. 插入初始化数据（8大分类）
-- =====================================================
INSERT INTO `service_category` (`categoryName`, `categoryCode`, `icon`, `sortOrder`) VALUES
                                                                                         ('医疗保障', 'medical', 'fa-heartbeat', 1),
                                                                                         ('社会保障', 'social', 'fa-users', 2),
                                                                                         ('住房保障', 'housing', 'fa-home', 3),
                                                                                         ('就业创业', 'employment', 'fa-briefcase', 4),
                                                                                         ('教育考试', 'education', 'fa-graduation-cap', 5),
                                                                                         ('交通出行', 'transport', 'fa-car', 6),
                                                                                         ('婚育养老', 'family', 'fa-child', 7),
                                                                                         ('生活服务', 'life', 'fa-coffee', 8);

-- =====================================================
-- 5. 插入示例服务数据
-- =====================================================
INSERT INTO `service_item` (`categoryId`, `serviceName`, `briefDesc`, `materialList`, `processDesc`, `baseInfo`, `viewCount`, `applyCount`) VALUES
                                                                                                                                                (1, '生育保险生育津贴申领', '女职工生育后申领生育津贴', '["身份证","出生医学证明","医院收费票据","生育服务证"]', '1. 提交申请\n2. 材料审核\n3. 待遇核算\n4. 发放津贴', '{"timeLimit":"15个工作日","fee":"免费","phone":"12345"}', 1280, 56),
                                                                                                                                                (1, '基本医疗保险报销', '医疗费用手工报销申请', '["身份证","医疗费用发票","费用明细清单","诊断证明"]', '1. 提交材料\n2. 审核核算\n3. 报销打款', '{"timeLimit":"20个工作日","fee":"免费","phone":"12345"}', 2340, 128),
                                                                                                                                                (2, '失业保险金申领', '失业人员申领失业保险金', '["身份证","离职证明","社保卡","银行卡"]', '1. 网上申请\n2. 资格审核\n3. 按月发放', '{"timeLimit":"5个工作日","fee":"免费","phone":"12333"}', 890, 34),
                                                                                                                                                (2, '城乡居民养老保险参保', '城乡居民养老保险参保登记', '["身份证","户口簿","银行卡"]', '1. 提交申请\n2. 资格审核\n3. 参保登记', '{"timeLimit":"即时办理","fee":"按年缴费","phone":"12333"}', 1560, 89),
                                                                                                                                                (3, '公租房资格申请', '申请公共租赁住房资格', '["身份证","收入证明","社保证明","婚姻证明"]', '1. 提交申请\n2. 资格审核\n3. 摇号配租', '{"timeLimit":"30个工作日","fee":"免费","phone":"12329"}', 3200, 210),
                                                                                                                                                (3, '住房公积金提取', '购买自住住房提取公积金', '["身份证","购房合同","发票","银行卡"]', '1. 网上申请\n2. 系统审核\n3. 资金到账', '{"timeLimit":"3个工作日","fee":"免费","phone":"12329"}', 4560, 342),
                                                                                                                                                (4, '就业失业登记', '办理就业失业登记证', '["身份证","户口簿","毕业证","照片"]', '1. 提交申请\n2. 审核信息\n3. 发证', '{"timeLimit":"7个工作日","fee":"免费","phone":"12333"}', 670, 23),
                                                                                                                                                (5, '高考成绩证明', '开具普通高考成绩证明', '["身份证","准考证","考生号"]', '1. 网上申请\n2. 信息核验\n3. 下载打印', '{"timeLimit":"1个工作日","fee":"免费","phone":"12345"}', 890, 67);

-- =====================================================
-- 6. 插入示例申请记录
-- =====================================================
INSERT INTO `service_application` (`applicationNo`, `userId`, `serviceId`, `region`, `applicantName`, `applicantPhone`, `status`) VALUES
                                                                                                                                      ('APP202603120001', 2029515503802257409, 1, '重庆市南岸区', '张三', '13800138000', 2),
                                                                                                                                      ('APP202603110002', 2029515503802257409, 3, '重庆市渝中区', '张三', '13800138000', 1),
                                                                                                                                      ('APP202603100003', 2029515503802257409, 5, '重庆市江北区', '张三', '13800138000', 3);

-- =====================================================
-- 7. 添加外键约束
-- =====================================================
ALTER TABLE `service_item`
    ADD CONSTRAINT `fk_service_item_category`
        FOREIGN KEY (`categoryId`) REFERENCES `service_category` (`id`) ON DELETE CASCADE;

ALTER TABLE `service_application`
    ADD CONSTRAINT `fk_service_application_service`
        FOREIGN KEY (`serviceId`) REFERENCES `service_item` (`id`) ON DELETE CASCADE;