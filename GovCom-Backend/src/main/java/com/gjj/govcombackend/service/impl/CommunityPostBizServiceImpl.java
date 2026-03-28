package com.gjj.govcombackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.govcombackend.exception.BusinessException;
import com.gjj.govcombackend.exception.ErrorCode;
import com.gjj.govcombackend.model.dto.community.PostCreateRequest;
import com.gjj.govcombackend.model.entity.CommunityPost;
import com.gjj.govcombackend.model.entity.CommunityComment;
import com.gjj.govcombackend.model.entity.CommunityLike;
import com.gjj.govcombackend.model.entity.User;
import com.gjj.govcombackend.model.vo.CommunityPostVO;
import com.gjj.govcombackend.model.vo.CommunityCommentVO;
import com.gjj.govcombackend.service.CommunityPostBizService;
import com.gjj.govcombackend.service.CommunityPostService;
import com.gjj.govcombackend.service.CommunityCommentService;
import com.gjj.govcombackend.service.CommunityLikeService;
import com.gjj.govcombackend.service.UserService;
import com.gjj.govcombackend.service.CommunityService;
import cn.hutool.json.JSONUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommunityPostBizServiceImpl implements CommunityPostBizService {

    @Resource
    private CommunityPostService postService;

    @Resource
    private CommunityCommentService commentService;

    @Resource
    private CommunityLikeService likeService;

    @Resource
    private UserService userService;

    @Resource
    private CommunityService communityService;

    @Override
    public List<CommunityPostVO> getPostList(Integer communityId, Integer type, Integer pageNum, Integer pageSize, Long userId) {
        Page<CommunityPost> page = new Page<>(pageNum, pageSize);
        QueryWrapper<CommunityPost> wrapper = new QueryWrapper<>();

        wrapper.eq("status", 1);

        if (communityId != null) {
            wrapper.eq("communityId", communityId);
        }

        if (type != null) {
            wrapper.eq("type", type);
        }

        wrapper.orderByDesc("createTime");

        Page<CommunityPost> result = postService.page(page, wrapper);

        return result.getRecords().stream()
                .map(post -> convertToPostVO(post, userId))
                .collect(Collectors.toList());
    }

    @Override
    public CommunityPostVO getPostDetail(Integer postId, Long userId) {
        CommunityPost post = postService.getById(postId);
        if (post == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "帖子不存在");
        }

        // 增加浏览次数
        post.setViewCount(post.getViewCount() + 1);
        postService.updateById(post);

        return convertToPostVO(post, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createPost(PostCreateRequest request, Long userId) {
        // 参数校验
        if (StrUtil.isBlank(request.getTitle())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题不能为空");
        }
        if (StrUtil.isBlank(request.getContent())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容不能为空");
        }

        CommunityPost post = new CommunityPost();
        BeanUtils.copyProperties(request, post);
        post.setUserId(userId);
        post.setViewCount(0);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setStatus(1);

        // 使用 Hutool 处理图片JSON
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            post.setImages(JSONUtil.toJsonStr(request.getImages()));
        }

        boolean saved = postService.save(post);
        if (!saved) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "发布失败");
        }

        return post.getId();
    }

    @Override
    public List<CommunityCommentVO> getComments(Integer postId, Long userId) {
        // 查询所有一级评论
        QueryWrapper<CommunityComment> wrapper = new QueryWrapper<>();
        wrapper.eq("postId", postId)
                .eq("parentId", 0)
                .eq("status", 1)
                .orderByDesc("createTime");

        List<CommunityComment> comments = commentService.list(wrapper);

        return comments.stream()
                .map(comment -> convertToCommentVO(comment, userId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createComment(Integer postId, String content, Long userId, Integer parentId) {
        // 检查帖子是否存在
        CommunityPost post = postService.getById(postId);
        if (post == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "帖子不存在");
        }

        CommunityComment comment = new CommunityComment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setParentId(parentId == null ? 0 : parentId);
        comment.setLikeCount(0);
        comment.setStatus(1);

        boolean saved = commentService.save(comment);
        if (!saved) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "评论失败");
        }

        // 更新帖子的评论数
        post.setCommentCount(post.getCommentCount() + 1);
        postService.updateById(post);

        return comment.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean toggleLike(Integer postId, Integer commentId, Long userId) {
        if (postId == null && commentId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请指定要点赞的内容");
        }

        // 检查是否已点赞
        QueryWrapper<CommunityLike> wrapper = new QueryWrapper<>();
        wrapper.eq("userId", userId);
        if (postId != null) {
            wrapper.eq("postId", postId);
        } else {
            wrapper.eq("commentId", commentId);
        }

        CommunityLike like = likeService.getOne(wrapper);

        if (like == null) {
            // 点赞
            like = new CommunityLike();
            like.setUserId(userId);
            like.setPostId(postId);
            like.setCommentId(commentId);
            likeService.save(like);

            // 更新点赞数
            if (postId != null) {
                CommunityPost post = postService.getById(postId);
                post.setLikeCount(post.getLikeCount() + 1);
                postService.updateById(post);
            } else {
                CommunityComment comment = commentService.getById(commentId);
                comment.setLikeCount(comment.getLikeCount() + 1);
                commentService.updateById(comment);
            }

            return true;
        } else {
            // 取消点赞
            likeService.removeById(like.getId());

            // 更新点赞数
            if (postId != null) {
                CommunityPost post = postService.getById(postId);
                post.setLikeCount(Math.max(0, post.getLikeCount() - 1));
                postService.updateById(post);
            } else {
                CommunityComment comment = commentService.getById(commentId);
                comment.setLikeCount(Math.max(0, comment.getLikeCount() - 1));
                commentService.updateById(comment);
            }

            return false;
        }
    }

    // ============== 私有转换方法 ==============

    private CommunityPostVO convertToPostVO(CommunityPost post, Long currentUserId) {
        CommunityPostVO vo = new CommunityPostVO();
        BeanUtils.copyProperties(post, vo);

        // 使用 Hutool 处理图片JSON
        if (StrUtil.isNotBlank(post.getImages())) {
            vo.setImages(JSONUtil.toList(post.getImages(), String.class));
        }

        // 设置类型名称
        vo.setTypeName(getTypeName(post.getType()));

        // 查询用户信息
        User user = userService.getById(post.getUserId());
        if (user != null) {
            vo.setUserName(user.getUserName());
            vo.setUserAvatar(user.getUserAvatar());
        }

        // 查询社区名称
        if (post.getCommunityId() != null) {
            com.gjj.govcombackend.model.entity.Community community =
                    communityService.getById(post.getCommunityId());
            if (community != null) {
                vo.setCommunityName(community.getCommunityName());
            }
        }

        // 查询当前用户是否点赞
        if (currentUserId != null) {
            QueryWrapper<CommunityLike> wrapper = new QueryWrapper<>();
            wrapper.eq("userId", currentUserId)
                    .eq("postId", post.getId());
            vo.setIsLiked(likeService.count(wrapper) > 0);
        }

        return vo;
    }

    private CommunityCommentVO convertToCommentVO(CommunityComment comment, Long currentUserId) {
        CommunityCommentVO vo = new CommunityCommentVO();
        BeanUtils.copyProperties(comment, vo);

        // 查询用户信息
        User user = userService.getById(comment.getUserId());
        if (user != null) {
            vo.setUserName(user.getUserName());
            vo.setUserAvatar(user.getUserAvatar());
        }

        // 查询当前用户是否点赞
        if (currentUserId != null) {
            QueryWrapper<CommunityLike> wrapper = new QueryWrapper<>();
            wrapper.eq("userId", currentUserId)
                    .eq("commentId", comment.getId());
            vo.setIsLiked(likeService.count(wrapper) > 0);
        }

        // 查询子评论（回复）
        QueryWrapper<CommunityComment> replyWrapper = new QueryWrapper<>();
        replyWrapper.eq("parentId", comment.getId())
                .eq("status", 1)
                .orderByAsc("createTime");

        List<CommunityComment> replies = commentService.list(replyWrapper);
        if (!replies.isEmpty()) {
            vo.setReplies(replies.stream()
                    .map(reply -> convertToCommentVO(reply, currentUserId))
                    .collect(Collectors.toList()));
        }

        return vo;
    }

    private String getTypeName(Integer type) {
        if (type == null) return "未知";
        switch (type) {
            case 1: return "邻里互助";
            case 2: return "二手交易";
            case 3: return "社区活动";
            case 4: return "闲聊交流";
            default: return "其他";
        }
    }
}