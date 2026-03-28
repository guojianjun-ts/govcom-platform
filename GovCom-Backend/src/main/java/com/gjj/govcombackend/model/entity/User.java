package com.gjj.govcombackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;


/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public  class User {
    /**
     * 用户ID（主键）
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 性别:0-男;1-女
     */
    private Integer gender;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像URL
     */
    private String userAvatar;

    /**
     * 用户类型：1-普通用户，2-政务人员，3-社区管理员，4-系统管理员
     */
    private Integer userType;

    /**
     * 状态：1-正常，0-禁用
     */
    private Integer status;

    /**
     * 逻辑删除标识:0-未删除;1-已删除
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}