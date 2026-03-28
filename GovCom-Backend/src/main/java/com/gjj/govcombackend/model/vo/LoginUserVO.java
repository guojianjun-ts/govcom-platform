package com.gjj.govcombackend.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class LoginUserVO implements Serializable {

    /**
     * 用户 id
     */
    @ApiModelProperty(value = "用户id")
    private Long id;

    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    private String userAccount;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String userName;

    /**
     * 性别:0-男;1-女
     */
    @ApiModelProperty(value = "性别:0-男;1-女")
    private Integer gender;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String userAvatar;

    /**
     * 用户类型：1-普通用户，2-政务人员，3-社区管理员，4-系统管理员
     */
    @ApiModelProperty(value = "用户类型：1-普通用户，2-政务人员，3-社区管理员，4-系统管理员")
    private Integer userType;

    /**
     * 用户状态：0-正常，1-禁用
     */
    @ApiModelProperty(value = "用户状态：0-正常，1-禁用")
    private Integer status;

    private static final long serialVersionUID = 1L;
}