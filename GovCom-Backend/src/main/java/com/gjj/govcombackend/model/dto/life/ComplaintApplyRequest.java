package com.gjj.govcombackend.model.dto.life;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ComplaintApplyRequest {

    @ApiModelProperty(value = "投诉类型：environmental-环境卫生，management-社区管理，facility-设施维护，other-其他")
    private String subType;

    @ApiModelProperty(value = "投诉标题")
    private String title;

    @ApiModelProperty(value = "详细描述")
    private String description;

    @ApiModelProperty(value = "发生地点")
    private String location;

    @ApiModelProperty(value = "申请人姓名")
    private String applicantName;

    @ApiModelProperty(value = "联系电话")
    private String applicantPhone;

    @ApiModelProperty(value = "附件列表")
    private java.util.List<String> attachments;
}