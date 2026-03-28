// InfoArticleVO.java
package com.gjj.govcombackend.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@Data
public class InfoArticleVO {

    @ApiModelProperty(value = "文章ID")
    private Integer id;

    @ApiModelProperty(value = "分类ID")
    private Integer categoryId;

    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "封面图")
    private String cover;

    @ApiModelProperty(value = "内容（支持HTML）")
    private String content;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "来源")
    private String source;

    @ApiModelProperty(value = "发布范围：1-全市，2-社区")
    private Integer scope;

    @ApiModelProperty(value = "社区ID（当scope=2时）")
    private Integer communityId;

    @ApiModelProperty(value = "社区名称")
    private String communityName;

    @ApiModelProperty(value = "标签")
    private String tag;

    @ApiModelProperty(value = "阅读次数")
    private Integer viewCount;

    @ApiModelProperty(value = "点赞次数")
    private Integer likeCount;

    @ApiModelProperty(value = "发布时间")
    private Date publishTime;

    @ApiModelProperty(value = "是否置顶")
    private Boolean isTop;
}