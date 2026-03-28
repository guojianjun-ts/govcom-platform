package com.gjj.govcombackend.controller;

import com.gjj.govcombackend.annotion.UserAuthCheck;
import com.gjj.govcombackend.common.BaseResponse;
import com.gjj.govcombackend.common.ResultUtils;
import com.gjj.govcombackend.exception.ErrorCode;
import com.gjj.govcombackend.exception.ThrowUtils;
import com.gjj.govcombackend.model.dto.life.ProofApplyRequest;
import com.gjj.govcombackend.model.dto.life.HelpApplyRequest;
import com.gjj.govcombackend.model.dto.life.ComplaintApplyRequest;
import com.gjj.govcombackend.model.dto.life.WorkOrderQueryRequest;
import com.gjj.govcombackend.model.entity.User;
import com.gjj.govcombackend.model.vo.WorkOrderVO;
import com.gjj.govcombackend.service.WorkOrderBizService;
import com.gjj.govcombackend.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/life")
public class LifeController {

    @Resource
    private WorkOrderBizService workOrderBizService;

    @Resource
    private UserService userService;

    // ============== 社区证明 ==============

    @PostMapping("/proof/submit")
    @ApiOperation("提交社区证明申请")
    @UserAuthCheck
    public BaseResponse<Integer> submitProof(
            @RequestBody  ProofApplyRequest request,
            HttpServletRequest httpRequest) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(httpRequest);
        Integer orderId = workOrderBizService.submitProof(request, loginUser.getId());
        return ResultUtils.success(orderId);
    }

    // ============== 社区帮助 ==============

    @PostMapping("/help/submit")
    @ApiOperation("提交社区帮助申请")
    @UserAuthCheck
    public BaseResponse<Integer> submitHelp(
            @RequestBody  HelpApplyRequest request,
            HttpServletRequest httpRequest) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(httpRequest);
        Integer orderId = workOrderBizService.submitHelp(request, loginUser.getId());
        return ResultUtils.success(orderId);
    }

    // ============== 投诉建议 ==============

    @PostMapping("/complaint/submit")
    @ApiOperation("提交投诉建议")
    @UserAuthCheck
    public BaseResponse<Integer> submitComplaint(
            @RequestBody  ComplaintApplyRequest request,
            HttpServletRequest httpRequest) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(httpRequest);
        Integer orderId = workOrderBizService.submitComplaint(request, loginUser.getId());
        return ResultUtils.success(orderId);
    }

    // ============== 工单查询（供个人中心使用） ==============

    @PostMapping("/order/list")
    @ApiOperation("获取我的申请列表")
    @UserAuthCheck
    public BaseResponse<List<WorkOrderVO>> getMyOrders(
            @RequestBody(required = false) WorkOrderQueryRequest request,
            HttpServletRequest httpRequest) {
        if (request == null) {
            request = new WorkOrderQueryRequest();
        }
        User loginUser = userService.getLoginUser(httpRequest);
        List<WorkOrderVO> list = workOrderBizService.getUserOrders(request, loginUser.getId());
        return ResultUtils.success(list);
    }

    @GetMapping("/order/detail/{id}")
    @ApiOperation("获取申请详情")
    @UserAuthCheck
    public BaseResponse<WorkOrderVO> getOrderDetail(
            @PathVariable Integer id,
            HttpServletRequest httpRequest) {
        ThrowUtils.throwIf(id == null, ErrorCode.PARAMS_ERROR, "工单ID不能为空");
        User loginUser = userService.getLoginUser(httpRequest);
        WorkOrderVO vo = workOrderBizService.getOrderDetail(id, loginUser.getId());
        return ResultUtils.success(vo);
    }

    // ============== 快捷查询（按类型） ==============

    @GetMapping("/order/proofs")
    @ApiOperation("获取我的社区证明列表")
    @UserAuthCheck
    public BaseResponse<List<WorkOrderVO>> getMyProofs(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest httpRequest) {
        WorkOrderQueryRequest request = new WorkOrderQueryRequest();
        request.setType(1);
        request.setPageNum(pageNum);
        request.setPageSize(pageSize);

        User loginUser = userService.getLoginUser(httpRequest);
        List<WorkOrderVO> list = workOrderBizService.getUserOrders(request, loginUser.getId());
        return ResultUtils.success(list);
    }

    @GetMapping("/order/helps")
    @ApiOperation("获取我的社区帮助列表")
    @UserAuthCheck
    public BaseResponse<List<WorkOrderVO>> getMyHelps(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest httpRequest) {
        WorkOrderQueryRequest request = new WorkOrderQueryRequest();
        request.setType(2);
        request.setPageNum(pageNum);
        request.setPageSize(pageSize);

        User loginUser = userService.getLoginUser(httpRequest);
        List<WorkOrderVO> list = workOrderBizService.getUserOrders(request, loginUser.getId());
        return ResultUtils.success(list);
    }

    @GetMapping("/order/complaints")
    @ApiOperation("获取我的投诉建议列表")
    @UserAuthCheck
    public BaseResponse<List<WorkOrderVO>> getMyComplaints(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest httpRequest) {
        WorkOrderQueryRequest request = new WorkOrderQueryRequest();
        request.setType(3);
        request.setPageNum(pageNum);
        request.setPageSize(pageSize);

        User loginUser = userService.getLoginUser(httpRequest);
        List<WorkOrderVO> list = workOrderBizService.getUserOrders(request, loginUser.getId());
        return ResultUtils.success(list);
    }
}