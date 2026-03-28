-- 使用数据库
USE `pu_yu`;

-- =====================================================
-- 工单主表 (work_order)
-- 统一存储所有类型的申请：社区证明、社区帮助、投诉建议
-- =====================================================
DROP TABLE IF EXISTS `work_order`;
CREATE TABLE `work_order` (
                              `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                              `orderNo` VARCHAR(50) NOT NULL COMMENT '工单编号',
                              `userId` BIGINT NOT NULL COMMENT '申请人ID',
                              `type` TINYINT NOT NULL COMMENT '工单类型：1-社区证明，2-社区帮助，3-投诉建议',
                              `subType` VARCHAR(50) COMMENT '子类型（如：residence/repair/environmental等）',
                              `title` VARCHAR(200) NOT NULL COMMENT '申请标题',
                              `content` TEXT COMMENT '详细描述',
                              `applicantName` VARCHAR(50) COMMENT '申请人姓名',
                              `applicantPhone` VARCHAR(20) COMMENT '联系电话',
                              `attachments` TEXT COMMENT '附件（JSON数组，存储图片URL）',
                              `formData` JSON COMMENT '表单数据（存储各类型特有的字段）',
                              `status` TINYINT DEFAULT 1 COMMENT '状态：1-待受理，2-处理中，3-已完成，4-已驳回',
                              `handlerId` BIGINT COMMENT '处理人ID',
                              `handlerName` VARCHAR(50) COMMENT '处理人姓名',
                              `handleTime` DATETIME COMMENT '处理时间',
                              `result` TEXT COMMENT '处理结果',
                              `resultFiles` TEXT COMMENT '结果文件（如证明PDF）',
                              `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `updateTime` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              `isDeleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                              INDEX `idx_user` (`userId`),
                              INDEX `idx_type` (`type`),
                              INDEX `idx_status` (`status`),
                              INDEX `idx_order_no` (`orderNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单主表';

-- =====================================================
-- 工单处理记录表 (work_order_log)
-- 记录工单状态变更和处理过程
-- =====================================================
DROP TABLE IF EXISTS `work_order_log`;
CREATE TABLE `work_order_log` (
                                  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                  `orderId` INT NOT NULL COMMENT '工单ID',
                                  `operator` VARCHAR(50) COMMENT '操作人',
                                  `operatorId` BIGINT COMMENT '操作人ID',
                                  `action` VARCHAR(50) COMMENT '操作：submit/accept/process/complete/reject',
                                  `fromStatus` TINYINT COMMENT '原状态',
                                  `toStatus` TINYINT COMMENT '新状态',
                                  `remark` VARCHAR(500) COMMENT '备注',
                                  `createTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  INDEX `idx_order` (`orderId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单处理记录表';