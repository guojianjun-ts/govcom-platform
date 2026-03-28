package com.gjj.govcombackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 工单处理记录表
 * @TableName work_order_log
 */
@TableName(value ="work_order_log")
@Data
public class WorkOrderLog {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 工单ID
     */
    private Integer orderId;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作：submit/accept/process/complete/reject
     */
    private String action;

    /**
     * 原状态
     */
    private Integer fromStatus;

    /**
     * 新状态
     */
    private Integer toStatus;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;
}