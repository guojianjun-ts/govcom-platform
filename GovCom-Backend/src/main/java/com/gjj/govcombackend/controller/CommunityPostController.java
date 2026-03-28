// CommunityPostController.java
package com.gjj.govcombackend.controller;

import com.gjj.govcombackend.annotion.UserAuthCheck;
import com.gjj.govcombackend.common.BaseResponse;
import com.gjj.govcombackend.common.ResultUtils;
import com.gjj.govcombackend.exception.ErrorCode;
import com.gjj.govcombackend.exception.ThrowUtils;
import com.gjj.govcombackend.model.dto.community.PostCreateRequest;
import com.gjj.govcombackend.model.entity.User;
import com.gjj.govcombackend.model.vo.CommunityPostVO;
import com.gjj.govcombackend.model.vo.CommunityCommentVO;
import com.gjj.govcombackend.service.CommunityPostBizService;
import com.gjj.govcombackend.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/community/post")
public class CommunityPostController {

    @Resource
    private CommunityPostBizService communityPostBizService;

    @Resource
    private UserService userService;

    @GetMapping("/list")
    @ApiOperation("获取帖子列表")
    public BaseResponse<List<CommunityPostVO>> getPostList(
            @RequestParam(required = false) Integer communityId,
            @RequestParam(required = false) Integer type,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {

        Long userId = getUserId(request);
        return ResultUtils.success(communityPostBizService.getPostList(communityId, type, pageNum, pageSize, userId));
    }

    @GetMapping("/detail/{id}")
    @ApiOperation("获取帖子详情")
    public BaseResponse<CommunityPostVO> getPostDetail(
            @PathVariable Integer id,
            HttpServletRequest request) {
        ThrowUtils.throwIf(id == null, ErrorCode.PARAMS_ERROR);
        Long userId = getUserId(request);
        return ResultUtils.success(communityPostBizService.getPostDetail(id, userId));
    }

    @PostMapping("/create")
    @ApiOperation("发布帖子")
    @UserAuthCheck
    public BaseResponse<Integer> createPost(
            @RequestBody PostCreateRequest request,
            HttpServletRequest httpRequest) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(httpRequest);
        return ResultUtils.success(communityPostBizService.createPost(request, loginUser.getId()));
    }

    @GetMapping("/comments/{postId}")
    @ApiOperation("获取评论列表")
    public BaseResponse<List<CommunityCommentVO>> getComments(
            @PathVariable Integer postId,
            HttpServletRequest request) {
        ThrowUtils.throwIf(postId == null, ErrorCode.PARAMS_ERROR);
        Long userId = getUserId(request);
        return ResultUtils.success(communityPostBizService.getComments(postId, userId));
    }

    @PostMapping("/comment/{postId}")
    @ApiOperation("发表评论")
    @UserAuthCheck
    public BaseResponse<Integer> createComment(
            @PathVariable Integer postId,
            @RequestParam String content,
            @RequestParam(required = false) Integer parentId,
            HttpServletRequest httpRequest) {
        ThrowUtils.throwIf(postId == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(content == null || content.trim().isEmpty(), ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(httpRequest);
        return ResultUtils.success(communityPostBizService.createComment(postId, content, loginUser.getId(), parentId));
    }

    @PostMapping("/like")
    @ApiOperation("点赞/取消点赞")
    @UserAuthCheck
    public BaseResponse<Boolean> toggleLike(
            @RequestParam(required = false) Integer postId,
            @RequestParam(required = false) Integer commentId,
            HttpServletRequest httpRequest) {
        ThrowUtils.throwIf(postId == null && commentId == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(httpRequest);
        return ResultUtils.success(communityPostBizService.toggleLike(postId, commentId, loginUser.getId()));
    }

    private Long getUserId(HttpServletRequest request) {
        try {
            User loginUser = userService.getLoginUser(request);
            return loginUser.getId();
        } catch (Exception e) {
            return null;
        }
    }
}