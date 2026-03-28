package com.gjj.govcombackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 资讯内容表
 * @TableName info_article
 */
@TableName(value ="info_article")
@Data
public class InfoArticle {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 标题
     */
    private String title;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 内容（支持HTML）
     */
    private String content;

    /**
     * 封面图
     */
    private String cover;

    /**
     * 作者
     */
    private String author;

    /**
     * 来源
     */
    private String source;

    /**
     * 发布范围：1-全市，2-指定社区
     */
    private Integer scope;

    /**
     * 指定社区ID（当scope=2时）
     */
    private Integer communityId;

    /**
     * 标签（多个用逗号分隔）
     */
    private String tag;

    /**
     * 阅读次数
     */
    private Integer viewCount;

    /**
     * 点赞次数
     */
    private Integer likeCount;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 是否置顶：0-否，1-是
     */
    private Integer isTop;

    /**
     * 状态：0-草稿，1-已发布，2-下架
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除：0-未删除，1-已删除
     */
    private Integer isDeleted;
}