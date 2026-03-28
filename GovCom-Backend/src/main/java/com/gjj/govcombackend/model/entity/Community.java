package com.gjj.govcombackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 社区表
 * @TableName community
 */
@TableName(value ="community")
@Data
public class Community {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 社区名称
     */
    private String communityName;

    /**
     * 社区编码
     */
    private String communityCode;

    /**
     * 社区地址
     */
    private String address;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 物业公司
     */
    private String propertyCompany;

    /**
     * 社区简介
     */
    private String intro;

    /**
     * 状态：0-禁用，1-启用
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