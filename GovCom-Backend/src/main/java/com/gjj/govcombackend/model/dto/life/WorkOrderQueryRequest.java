package com.gjj.govcombackend.model.dto.life;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WorkOrderQueryRequest {

    @ApiModelProperty(value = "工单类型：1-社区证明，2-社区帮助，3-投诉建议")
    private Integer type;

    @ApiModelProperty(value = "状态：1-待受理，2-处理中，3-已完成，4-已驳回")
    private Integer status;

    @ApiModelProperty(value = "页码", example = "1")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页大小", example = "10")
    private Integer pageSize = 10;
}