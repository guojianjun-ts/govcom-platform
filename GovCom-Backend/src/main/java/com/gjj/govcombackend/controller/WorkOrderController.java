package com.gjj.govcombackend.controller;

import com.gjj.govcombackend.annotion.UserAuthCheck;
import com.gjj.govcombackend.common.BaseResponse;
import com.gjj.govcombackend.common.ResultUtils;
import com.gjj.govcombackend.exception.BusinessException;
import com.gjj.govcombackend.exception.ErrorCode;
import com.gjj.govcombackend.exception.ThrowUtils;
import com.gjj.govcombackend.model.dto.life.WorkOrderQueryRequest;
import com.gjj.govcombackend.model.dto.workOrder.WorkOrderProcessRequest;
import com.gjj.govcombackend.model.entity.User;
import com.gjj.govcombackend.model.vo.WorkOrderVO;
import com.gjj.govcombackend.service.WorkOrderBizService;
import com.gjj.govcombackend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/workOrder")
public class WorkOrderController {

    @Resource
    private WorkOrderBizService workOrderBizService;

    @Resource
    private UserService userService;

    // ============== 政务工单 ==============

    @PostMapping("/gov/list")
    @ApiOperation("获取政务工单列表")
    @UserAuthCheck
    public BaseResponse<List<WorkOrderVO>> getGovWorkOrders(
            @RequestBody WorkOrderQueryRequest request,
            HttpServletRequest httpRequest) {
        if (request == null) {
            request = new WorkOrderQueryRequest();
        }

        User loginUser = userService.getLoginUser(httpRequest);
        if (loginUser.getUserType() != 2 && loginUser.getUserType() != 4) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权访问");
        }
        List<WorkOrderVO> list = workOrderBizService.getGovWorkOrders(request, loginUser);
        return ResultUtils.success(list);
    }

    // ============== 社区工单 ==============

    @PostMapping("/community/list")
    @ApiOperation("获取社区工单列表")
    @UserAuthCheck
    public BaseResponse<List<WorkOrderVO>> getCommunityWorkOrders(
            @RequestBody WorkOrderQueryRequest request,
            HttpServletRequest httpRequest) {
        if (request == null) {
            request = new WorkOrderQueryRequest();
        }

        User loginUser = userService.getLoginUser(httpRequest);
        if (loginUser.getUserType() != 3 && loginUser.getUserType() != 4) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权访问");
        }
        List<WorkOrderVO> list = workOrderBizService.getCommunityWorkOrders(request, loginUser);
        return ResultUtils.success(list);
    }

    // ============== 工单详情 ==============

    @GetMapping("/detail/{id}")
    @ApiOperation("获取工单详情")
    @UserAuthCheck
    public BaseResponse<WorkOrderVO> getWorkOrderDetail(
            @PathVariable Integer id,
            HttpServletRequest httpRequest) {
        ThrowUtils.throwIf(id == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(httpRequest);
        WorkOrderVO vo = workOrderBizService.getWorkOrderDetail(id, loginUser);
        return ResultUtils.success(vo);
    }

    // ============== 处理工单 ==============

    @PostMapping("/process")
    @ApiOperation("处理工单")
    @UserAuthCheck
    public BaseResponse<Boolean> processWorkOrder(
            @RequestBody WorkOrderProcessRequest request,
            HttpServletRequest httpRequest) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(request.getId() == null, ErrorCode.PARAMS_ERROR, "工单ID不能为空");
        ThrowUtils.throwIf(request.getResult() == null || request.getResult().trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "处理结果不能为空");
        ThrowUtils.throwIf(request.getStatus() == null ||
                        (request.getStatus() != 3 && request.getStatus() != 4),
                ErrorCode.PARAMS_ERROR, "处理状态不正确");

        User loginUser = userService.getLoginUser(httpRequest);
        boolean result = workOrderBizService.processWorkOrder(request, loginUser);
        return ResultUtils.success(result);
    }
}