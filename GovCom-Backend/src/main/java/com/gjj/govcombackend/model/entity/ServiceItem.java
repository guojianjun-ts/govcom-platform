package com.gjj.govcombackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 服务事项表
 * @TableName service_item
 */
@TableName(value ="service_item")
@Data
public class ServiceItem {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 所属分类ID
     */
    private Integer categoryId;

    /**
     * 服务名称（如：生育保险生育津贴申领）
     */
    private String serviceName;

    /**
     * 服务编码
     */
    private String serviceCode;

    /**
     * 简短描述
     */
    private String briefDesc;

    /**
     * 图标名称
     */
    private String icon;

    /**
     * 办理材料（JSON数组格式，如：["身份证","出生证明","医院单据"]）
     */
    private String materialList;

    /**
     * 办理流程描述
     */
    private String processDesc;

    /**
     * 基本信息（办理时限、费用等，JSON格式）
     */
    private String baseInfo;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 申请次数
     */
    private Integer applyCount;

    /**
     * 状态：0-下架，1-上架
     */
    private Integer status;

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