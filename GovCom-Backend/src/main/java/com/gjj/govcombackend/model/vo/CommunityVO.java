// CommunityVO.java
package com.gjj.govcombackend.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CommunityVO {

    @ApiModelProperty(value = "社区ID")
    private Integer id;

    @ApiModelProperty(value = "社区名称")
    private String communityName;

    @ApiModelProperty(value = "社区编码")
    private String communityCode;

    @ApiModelProperty(value = "社区地址")
    private String address;

    @ApiModelProperty(value = "联系电话")
    private String contactPhone;

    @ApiModelProperty(value = "物业公司")
    private String propertyCompany;

    @ApiModelProperty(value = "社区简介")
    private String intro;

    @ApiModelProperty(value = "是否已加入")
    private Boolean isJoined;
}
