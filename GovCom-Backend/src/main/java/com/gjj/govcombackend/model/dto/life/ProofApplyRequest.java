package com.gjj.govcombackend.model.dto.life;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProofApplyRequest {

    @ApiModelProperty(value = "证明类型：residence-居住证明，economic-经济状况证明，activity-活动证明，background-政审证明")
    private String subType;

    @ApiModelProperty(value = "学生姓名")
    private String studentName;

    @ApiModelProperty(value = "就读学校")
    private String school;

    @ApiModelProperty(value = "用途说明")
    private String purpose;

    @ApiModelProperty(value = "申请人姓名")
    private String applicantName;

    @ApiModelProperty(value = "联系电话")
    private String applicantPhone;

    @ApiModelProperty(value = "附件列表")
    private java.util.List<String> attachments;
}