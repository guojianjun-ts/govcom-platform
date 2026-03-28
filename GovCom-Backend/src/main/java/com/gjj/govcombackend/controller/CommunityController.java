// CommunityController.java
package com.gjj.govcombackend.controller;

import com.gjj.govcombackend.annotion.UserAuthCheck;
import com.gjj.govcombackend.common.BaseResponse;
import com.gjj.govcombackend.common.ResultUtils;
import com.gjj.govcombackend.exception.ErrorCode;
import com.gjj.govcombackend.exception.ThrowUtils;
import com.gjj.govcombackend.model.entity.Community;
import com.gjj.govcombackend.model.entity.User;
import com.gjj.govcombackend.model.vo.CommunityVO;

import com.gjj.govcombackend.service.CommunityBizService;
import com.gjj.govcombackend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/community")
public class CommunityController {

    @Resource
    private CommunityBizService communityBizService;   // 注入业务服务

    @Resource
    private UserService userService;

    @GetMapping("/list")
    @ApiOperation("获取所有社区列表")
    public BaseResponse<List<CommunityVO>> getCommunityList() {
        return ResultUtils.success(communityBizService.getCommunityList());
    }

    @GetMapping("/my")
    @ApiOperation("获取我加入的社区")
    @UserAuthCheck
    public BaseResponse<List<CommunityVO>> getMyCommunities(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(communityBizService.getMyCommunities(loginUser.getId()));
    }

    @GetMapping("/detail/{id}")
    @ApiOperation("获取社区详情")
    public BaseResponse<Community> getCommunityDetail(@PathVariable Integer id) {
        ThrowUtils.throwIf(id == null, ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(communityBizService.getCommunityDetail(id));
    }

    @PostMapping("/join/{communityId}")
    @ApiOperation("加入社区")
    @UserAuthCheck
    public BaseResponse<Boolean> joinCommunity(
            @PathVariable Integer communityId,
            HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        boolean result = communityBizService.joinCommunity(loginUser.getId(), communityId);
        return ResultUtils.success(result);
    }

    @PostMapping("/leave/{communityId}")
    @ApiOperation("退出社区")
    @UserAuthCheck
    public BaseResponse<Boolean> leaveCommunity(
            @PathVariable Integer communityId,
            HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        boolean result = communityBizService.leaveCommunity(loginUser.getId(), communityId);
        return ResultUtils.success(result);
    }
}