// CommunityPostVO.java
package com.gjj.govcombackend.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class CommunityPostVO {

    @ApiModelProperty(value = "帖子ID")
    private Integer id;

    @ApiModelProperty(value = "发布者ID")
    private Long userId;

    @ApiModelProperty(value = "发布者姓名")
    private String userName;

    @ApiModelProperty(value = "发布者头像")
    private String userAvatar;

    @ApiModelProperty(value = "所属社区ID")
    private Integer communityId;

    @ApiModelProperty(value = "所属社区名称")
    private String communityName;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "类型：1-互助，2-二手，3-活动，4-闲聊")
    private Integer type;

    @ApiModelProperty(value = "类型名称")
    private String typeName;

    @ApiModelProperty(value = "图片列表")
    private List<String> images;

    @ApiModelProperty(value = "浏览次数")
    private Integer viewCount;

    @ApiModelProperty(value = "点赞次数")
    private Integer likeCount;

    @ApiModelProperty(value = "评论次数")
    private Integer commentCount;

    @ApiModelProperty(value = "当前用户是否点赞")
    private Boolean isLiked;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}