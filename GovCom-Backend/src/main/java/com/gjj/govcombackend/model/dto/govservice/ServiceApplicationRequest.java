package com.gjj.govcombackend.model.dto.govservice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 服务申请请求参数
 */
@Data
public class ServiceApplicationRequest {

    /**
     * 服务ID
     */
    @ApiModelProperty(value = "服务ID")
    private Integer serviceId;

    /**
     * 地区（如：重庆市南岸区）
     */
    @ApiModelProperty(value = "地区")
    private String region;

    /**
     * 申请人姓名
     */
    @ApiModelProperty(value = "申请人姓名")
    private String applicantName;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String applicantPhone;

    /**
     * 是否同意用户协议
     */
    @ApiModelProperty(value = "是否同意用户协议")
    private Boolean agreeProtocol;
}