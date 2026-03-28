package com.gjj.govcombackend.model.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 修改密码请求
 */
@Data
public class UserUpdatePasswordRequest implements Serializable {

    @ApiModelProperty(value = "原密码", required = true)
    private String oldPassword;

    @ApiModelProperty(value = "新密码", required = true)
    private String newPassword;

    @ApiModelProperty(value = "确认新密码", required = true)
    private String checkPassword;

    private static final long serialVersionUID = 1L;
}