package com.gjj.govcombackend.model.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新请求
 */
@Data
public class UserUpdateRequest implements Serializable {

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
    @ApiModelProperty(value = "用户头像URL")
    private String userAvatar;

    private static final long serialVersionUID = 1L;
}