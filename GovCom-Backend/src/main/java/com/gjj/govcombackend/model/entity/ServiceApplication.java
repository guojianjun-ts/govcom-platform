package com.gjj.govcombackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 服务申请表
 * @TableName service_application
 */
@TableName(value ="service_application")
@Data
public class ServiceApplication {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 申请单号
     */
    private String applicationNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 服务ID
     */
    private Integer serviceId;

    /**
     * 地区（如：重庆市南岸区）
     */
    private String region;

    /**
     * 申请人姓名
     */
    private String applicantName;

    /**
     * 联系电话
     */
    private String applicantPhone;

    /**
     * 状态：1-已提交，2-审核中，3-已完成，4-已驳回
     */
    private Integer status;

    /**
     * 提交时间
     */
    private Date submitTime;

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