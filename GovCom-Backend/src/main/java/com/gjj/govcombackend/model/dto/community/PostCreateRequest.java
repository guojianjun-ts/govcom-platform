// PostCreateRequest.java
package com.gjj.govcombackend.model.dto.community;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
public class PostCreateRequest {

    @ApiModelProperty(value = "所属社区ID（可选）")
    private Integer communityId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "类型：1-互助，2-二手，3-活动，4-闲聊")
    private Integer type;

    @ApiModelProperty(value = "图片列表")
    private List<String> images;
}