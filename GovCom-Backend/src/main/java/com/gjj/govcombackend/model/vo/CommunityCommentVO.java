// CommunityCommentVO.java
package com.gjj.govcombackend.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class CommunityCommentVO {

    @ApiModelProperty(value = "评论ID")
    private Integer id;

    @ApiModelProperty(value = "帖子ID")
    private Integer postId;

    @ApiModelProperty(value = "评论者ID")
    private Long userId;

    @ApiModelProperty(value = "评论者姓名")
    private String userName;

    @ApiModelProperty(value = "评论者头像")
    private String userAvatar;

    @ApiModelProperty(value = "父评论ID")
    private Integer parentId;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "点赞次数")
    private Integer likeCount;

    @ApiModelProperty(value = "当前用户是否点赞")
    private Boolean isLiked;

    @ApiModelProperty(value = "子评论列表")
    private List<CommunityCommentVO> replies;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}