package com.gjj.govcombackend.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@Data
public class GovServiceApplicationVO {

    @ApiModelProperty(value = "申请ID")
    private Integer id;

    @ApiModelProperty(value = "申请单号")
    private String applicationNo;

    @ApiModelProperty(value = "服务ID")
    private Integer serviceId;

    @ApiModelProperty(value = "服务名称")
    private String serviceName;

    @ApiModelProperty(value = "服务类型")
    private String serviceType;

    @ApiModelProperty(value = "服务分类")
    private String categoryName;

    @ApiModelProperty(value = "地区")
    private String region;

    @ApiModelProperty(value = "申请人姓名")
    private String applicantName;

    @ApiModelProperty(value = "联系电话")
    private String applicantPhone;

    @ApiModelProperty(value = "状态：1-已提交 2-审核中 3-已完成 4-已驳回")
    private Integer status;

    @ApiModelProperty(value = "状态中文描述")
    private String statusText;

    @ApiModelProperty(value = "提交时间")
    private Date submitTime;

    @ApiModelProperty(value = "申请时间")
    private Date createTime;
}