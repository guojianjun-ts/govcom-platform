package com.gjj.govcombackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 工单主表
 * @TableName work_order
 */
@TableName(value ="com_work_order")
@Data
public class WorkOrder {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 工单编号
     */
    private String orderNo;

    /**
     * 申请人ID
     */
    private Long userId;

    /**
     * 工单类型：1-社区证明，2-社区帮助，3-投诉建议
     */
    private Integer type;

    /**
     * 子类型（如：residence/repair/environmental等）
     */
    private String subType;

    /**
     * 申请标题
     */
    private String title;

    /**
     * 详细描述
     */
    private String content;

    /**
     * 申请人姓名
     */
    private String applicantName;

    /**
     * 联系电话
     */
    private String applicantPhone;

    /**
     * 附件（JSON数组，存储图片URL）
     */
    private String attachments;

    /**
     * 表单数据（存储各类型特有的字段）
     */
    private String formData;

    /**
     * 状态：1-待受理，2-处理中，3-已完成，4-已驳回
     */
    private Integer status;

    /**
     * 处理人ID
     */
    private Long handlerId;

    /**
     * 处理人姓名
     */
    private String handlerName;

    /**
     * 处理时间
     */
    private Date handleTime;

    /**
     * 处理结果
     */
    private String result;

    /**
     * 结果文件（如证明PDF）
     */
    private String resultFiles;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除：0-未删除，1-已删除
     */
    private Integer isDeleted;
}