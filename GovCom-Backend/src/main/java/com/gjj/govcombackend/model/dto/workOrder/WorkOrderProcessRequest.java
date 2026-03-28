// WorkOrderProcessRequest.java
package com.gjj.govcombackend.model.dto.workOrder;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WorkOrderProcessRequest {

    @ApiModelProperty(value = "工单ID")
    private Integer id;

    @ApiModelProperty(value = "处理结果")
    private String result;

    @ApiModelProperty(value = "处理状态：3-已完成，4-已驳回")
    private Integer status;
}