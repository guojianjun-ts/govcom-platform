package com.gjj.govcombackend.controller;

import com.gjj.govcombackend.annotion.UserAuthCheck;
import com.gjj.govcombackend.common.BaseResponse;
import com.gjj.govcombackend.common.ResultUtils;
import com.gjj.govcombackend.exception.ErrorCode;
import com.gjj.govcombackend.exception.ThrowUtils;
import com.gjj.govcombackend.model.entity.ServiceCategory;
import com.gjj.govcombackend.model.entity.User;
import com.gjj.govcombackend.model.dto.govservice.ServiceApplicationRequest;
import com.gjj.govcombackend.model.vo.ServiceItemVO;
import com.gjj.govcombackend.model.vo.ServiceApplicationVO;
import com.gjj.govcombackend.service.GovService;
import com.gjj.govcombackend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/gov")
public class GovController {

    @Resource
    private GovService govService;

    @Resource
    private UserService userService;

    // ============== 分类接口 ==============

    @GetMapping("/category/list")
    @ApiOperation("获取服务分类列表")
    public BaseResponse<List<ServiceCategory>> getGovCategoryList() {
        List<ServiceCategory> list = govService.getCategoryList();
        return ResultUtils.success(list);
    }

    // ============== 服务事项接口 ==============

    @GetMapping("/service/list")
    @ApiOperation("获取分类下的服务列表")
    public BaseResponse<List<ServiceItemVO>> getServiceList(
            @RequestParam Integer categoryId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        List<ServiceItemVO> list = govService.getServiceList(categoryId, pageNum, pageSize);
        return ResultUtils.success(list);
    }

    @GetMapping("/service/detail/{id}")
    @ApiOperation("获取服务详情")
    public BaseResponse<ServiceItemVO> getServiceDetail(@PathVariable Integer id) {
        ServiceItemVO serviceItemVO = govService.getServiceDetail(id);
        return ResultUtils.success(serviceItemVO);
    }

    @GetMapping("/service/search")
    @ApiOperation("搜索服务")
    public BaseResponse<List<ServiceItemVO>> searchService(@RequestParam String keyword) {
        List<ServiceItemVO> list = govService.searchService(keyword);
        return ResultUtils.success(list);
    }

    @GetMapping("/service/hot")
    @ApiOperation("获取热门服务")
    public BaseResponse<List<ServiceItemVO>> getHotServices() {
        List<ServiceItemVO> list = govService.getHotServices();
        return ResultUtils.success(list);
    }

    // ============== 申请接口 ==============

    @PostMapping("/application/submit")
    @ApiOperation("提交服务申请")
    @UserAuthCheck
    public BaseResponse<Boolean> submitApplication(
            @RequestBody ServiceApplicationRequest request,
            HttpServletRequest httpRequest) {
        // 参数校验
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(request.getServiceId() == null, ErrorCode.PARAMS_ERROR, "服务ID不能为空");

        // 获取当前登录用户
        User loginUser = userService.getLoginUser(httpRequest);

        // 提交申请
        boolean result = govService.submitApplication(request, loginUser.getId());
        return ResultUtils.success(result);
    }

    @GetMapping("/application/my")
    @ApiOperation("获取我的申请列表")
    @UserAuthCheck
    public BaseResponse<List<ServiceApplicationVO>> getMyApplications(HttpServletRequest httpRequest) {
        // 获取当前登录用户
        User loginUser = userService.getLoginUser(httpRequest);

        // 查询我的申请列表
        List<ServiceApplicationVO> list = govService.getMyApplications(loginUser.getId());
        return ResultUtils.success(list);
    }

    @GetMapping("/application/detail/{id}")
    @ApiOperation("获取申请详情")
    @UserAuthCheck
    public BaseResponse<ServiceApplicationVO> getApplicationDetail(
            @PathVariable Integer id,
            HttpServletRequest httpRequest) {
        // 参数校验
        ThrowUtils.throwIf(id == null, ErrorCode.PARAMS_ERROR, "申请ID不能为空");

        // 获取当前登录用户
        User loginUser = userService.getLoginUser(httpRequest);

        // 查询申请详情
        ServiceApplicationVO vo = govService.getApplicationDetail(id, loginUser.getId());
        return ResultUtils.success(vo);
    }
}