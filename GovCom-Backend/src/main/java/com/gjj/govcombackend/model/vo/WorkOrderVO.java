package com.gjj.govcombackend.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class WorkOrderVO {

    @ApiModelProperty(value = "工单ID")
    private Integer id;

    @ApiModelProperty(value = "工单编号")
    private String orderNo;

    @ApiModelProperty(value = "工单类型：1-社区证明，2-社区帮助，3-投诉建议")
    private Integer type;

    @ApiModelProperty(value = "类型名称")
    private String typeName;

    @ApiModelProperty(value = "子类型编码")
    private String subType;

    @ApiModelProperty(value = "子类型名称")
    private String subTypeName;

    @ApiModelProperty(value = "申请标题")
    private String title;

    @ApiModelProperty(value = "详细描述")
    private String content;

    @ApiModelProperty(value = "申请人姓名")
    private String applicantName;

    @ApiModelProperty(value = "联系电话")
    private String applicantPhone;

    @ApiModelProperty(value = "附件列表")
    private List<String> attachments;

    @ApiModelProperty(value = "表单数据")
    private Map<String, Object> formData;

    @ApiModelProperty(value = "状态：1-待受理，2-处理中，3-已完成，4-已驳回")
    private Integer status;

    @ApiModelProperty(value = "状态名称")
    private String statusText;

    @ApiModelProperty(value = "提交时间")
    private Date createTime;

    @ApiModelProperty(value = "处理时间")
    private Date handleTime;

    @ApiModelProperty(value = "处理结果")
    private String result;

    @ApiModelProperty(value = "结果文件列表")
    private List<String> resultFiles;
}