package com.gjj.govcombackend.model.dto.life;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HelpApplyRequest {

    @ApiModelProperty(value = "帮助类型：repair-物业报修，consult-政策咨询，elderly-老人关怀，other-其他")
    private String subType;

    @ApiModelProperty(value = "详细描述")
    private String description;

    @ApiModelProperty(value = "预约时间")
    private String appointmentTime;

    @ApiModelProperty(value = "具体地址")
    private String address;

    @ApiModelProperty(value = "申请人姓名")
    private String applicantName;

    @ApiModelProperty(value = "联系电话")
    private String applicantPhone;

    @ApiModelProperty(value = "附件列表")
    private java.util.List<String> attachments;
}