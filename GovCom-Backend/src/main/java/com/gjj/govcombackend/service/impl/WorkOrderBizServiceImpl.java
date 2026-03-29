// WorkOrderBizServiceImpl.java
package com.gjj.govcombackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.govcombackend.exception.BusinessException;
import com.gjj.govcombackend.exception.ErrorCode;
import com.gjj.govcombackend.model.dto.life.ProofApplyRequest;
import com.gjj.govcombackend.model.dto.life.HelpApplyRequest;
import com.gjj.govcombackend.model.dto.life.ComplaintApplyRequest;
import com.gjj.govcombackend.model.dto.life.WorkOrderQueryRequest;
import com.gjj.govcombackend.model.dto.workOrder.GovWorkOrderProcessRequest;
import com.gjj.govcombackend.model.dto.workOrder.WorkOrderProcessRequest;
import com.gjj.govcombackend.model.entity.*;
import com.gjj.govcombackend.model.vo.GovServiceApplicationVO;
import com.gjj.govcombackend.model.vo.WorkOrderVO;
import com.gjj.govcombackend.service.*;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WorkOrderBizServiceImpl implements WorkOrderBizService {

    @Resource
    private ServiceApplicationService serviceApplicationService;

    @Resource
    private ServiceItemService serviceItemService;

    @Resource
    private InfoCategoryService infoCategoryService;

    @Resource
    private WorkOrderService workOrderService;

    @Resource
    private WorkOrderLogService workOrderLogService;

    @Resource
    private UserCommunityService userCommunityService;  // 用于获取用户所属社区

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer submitProof(ProofApplyRequest request, Long userId) {
        // 参数校验
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        }
        if (StrUtil.isBlank(request.getSubType())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请选择证明类型");
        }
        if (StrUtil.isBlank(request.getStudentName())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请输入学生姓名");
        }

        // 构建表单数据
        Map<String, Object> formData = new HashMap<>();
        formData.put("studentName", request.getStudentName());
        formData.put("school", request.getSchool());
        formData.put("purpose", request.getPurpose());

        // 创建工单
        WorkOrder workOrder = new WorkOrder();
        workOrder.setOrderNo(generateOrderNo());
        workOrder.setUserId(userId);
        workOrder.setType(1); // 1-社区证明
        workOrder.setSubType(request.getSubType());
        workOrder.setTitle(getProofTitle(request.getSubType()));
        workOrder.setApplicantName(request.getApplicantName());
        workOrder.setApplicantPhone(request.getApplicantPhone());
        workOrder.setFormData(JSONUtil.toJsonStr(formData));

        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
            workOrder.setAttachments(JSONUtil.toJsonStr(request.getAttachments()));
        }

        workOrder.setStatus(1); // 1-待受理

        boolean saved = workOrderService.save(workOrder);
        if (!saved) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "提交失败");
        }

        // 记录日志
        saveOrderLog(workOrder.getId(), userId, "submit", null, 1, "提交申请");

        return workOrder.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer submitHelp(HelpApplyRequest request, Long userId) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        }
        if (StrUtil.isBlank(request.getSubType())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请选择帮助类型");
        }
        if (StrUtil.isBlank(request.getDescription())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请输入详细描述");
        }

        Map<String, Object> formData = new HashMap<>();
        formData.put("description", request.getDescription());
        formData.put("appointmentTime", request.getAppointmentTime());
        formData.put("address", request.getAddress());

        WorkOrder workOrder = new WorkOrder();
        workOrder.setOrderNo(generateOrderNo());
        workOrder.setUserId(userId);
        workOrder.setType(2); // 2-社区帮助
        workOrder.setSubType(request.getSubType());
        workOrder.setTitle(getHelpTitle(request.getSubType()));
        workOrder.setApplicantName(request.getApplicantName());
        workOrder.setApplicantPhone(request.getApplicantPhone());
        workOrder.setFormData(JSONUtil.toJsonStr(formData));

        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
            workOrder.setAttachments(JSONUtil.toJsonStr(request.getAttachments()));
        }

        workOrder.setStatus(1);

        boolean saved = workOrderService.save(workOrder);
        if (!saved) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "提交失败");
        }

        saveOrderLog(workOrder.getId(), userId, "submit", null, 1, "提交申请");

        return workOrder.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer submitComplaint(ComplaintApplyRequest request, Long userId) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        }
        if (StrUtil.isBlank(request.getSubType())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请选择投诉类型");
        }
        if (StrUtil.isBlank(request.getTitle())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请输入投诉标题");
        }
        if (StrUtil.isBlank(request.getDescription())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请输入详细描述");
        }

        Map<String, Object> formData = new HashMap<>();
        formData.put("description", request.getDescription());
        formData.put("location", request.getLocation());

        WorkOrder workOrder = new WorkOrder();
        workOrder.setOrderNo(generateOrderNo());
        workOrder.setUserId(userId);
        workOrder.setType(3); // 3-投诉建议
        workOrder.setSubType(request.getSubType());
        workOrder.setTitle(request.getTitle());
        workOrder.setApplicantName(request.getApplicantName());
        workOrder.setApplicantPhone(request.getApplicantPhone());
        workOrder.setFormData(JSONUtil.toJsonStr(formData));

        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
            workOrder.setAttachments(JSONUtil.toJsonStr(request.getAttachments()));
        }

        workOrder.setStatus(1);

        boolean saved = workOrderService.save(workOrder);
        if (!saved) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "提交失败");
        }

        saveOrderLog(workOrder.getId(), userId, "submit", null, 1, "提交投诉建议");

        return workOrder.getId();
    }

    @Override
    public List<WorkOrderVO> getUserOrders(WorkOrderQueryRequest request, Long userId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录");
        }

        Page<WorkOrder> page = new Page<>(request.getPageNum(), request.getPageSize());
        QueryWrapper<WorkOrder> wrapper = new QueryWrapper<>();

        wrapper.eq("userId", userId);

        if (request.getType() != null) {
            wrapper.eq("type", request.getType());
        }

        if (request.getStatus() != null) {
            wrapper.eq("status", request.getStatus());
        }

        wrapper.orderByDesc("createTime");

        Page<WorkOrder> result = workOrderService.page(page, wrapper);

        return result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public GovServiceApplicationVO getGovWorkOrderDetail(Integer id) {
        ServiceApplication application = serviceApplicationService.getById(id);
        if (application == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        return convertToGovVO(application);
    }

    @Override
    public WorkOrderVO getOrderDetail(Integer id, Long userId) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "工单ID不能为空");
        }
        if (userId == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录");
        }

        WorkOrder order = workOrderService.getById(id);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }

        // 只能查看自己的工单
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权查看他人工单");
        }

        return convertToVO(order);
    }

    // ============== 新增方法实现 ==============

    @Override
    public List<WorkOrderVO> getGovWorkOrders(WorkOrderQueryRequest request, User loginUser) {
        // 政务人员查看所有工单（可根据需要按类型过滤）
        Page<WorkOrder> page = new Page<>(request.getPageNum(), request.getPageSize());
        QueryWrapper<WorkOrder> wrapper = new QueryWrapper<>();
        // 可根据业务添加筛选条件，例如只查看 type=1 的工单？这里暂不限制
        if (request.getType() != null) {
            wrapper.eq("type", request.getType());
        }
        if (request.getStatus() != null) {
            wrapper.eq("status", request.getStatus());
        }
        wrapper.orderByDesc("createTime");
        Page<WorkOrder> result = workOrderService.page(page, wrapper);
        return result.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public List<GovServiceApplicationVO> getGovWorkOrders(WorkOrderQueryRequest request) {
        Page<ServiceApplication> page = new Page<>(request.getPageNum(), request.getPageSize());
        QueryWrapper<ServiceApplication> wrapper = new QueryWrapper<>();

        if (request.getStatus() != null) {
            wrapper.eq("status", request.getStatus());
        }
        wrapper.orderByDesc("createTime");

        Page<ServiceApplication> result = serviceApplicationService.page(page, wrapper);

        return result.getRecords().stream()
                .map(this::convertToGovVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processGovWorkOrder(GovWorkOrderProcessRequest request) {
        ServiceApplication application = serviceApplicationService.getById(request.getId());
        if (application == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }

        // 只更新状态，不记录处理人、处理时间、处理结果
        application.setStatus(request.getStatus());

        boolean updated = serviceApplicationService.updateById(application);
        if (updated) {
            log.info("政务工单状态更新成功 - 工单ID: {}, 新状态: {}", request.getId(), request.getStatus());
        }

        return updated;
    }


    @Override
    public List<WorkOrderVO> getCommunityWorkOrders(WorkOrderQueryRequest request, User loginUser) {
        // 社区人员查看所有工单（或者根据业务需要按类型过滤）
        Page<WorkOrder> page = new Page<>(request.getPageNum(), request.getPageSize());
        QueryWrapper<WorkOrder> wrapper = new QueryWrapper<>();

        if (request.getType() != null) {
            wrapper.eq("type", request.getType());
        }
        if (request.getStatus() != null) {
            wrapper.eq("status", request.getStatus());
        }
        wrapper.orderByDesc("createTime");
        Page<WorkOrder> result = workOrderService.page(page, wrapper);
        return result.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public WorkOrderVO getWorkOrderDetail(Integer id, User loginUser) {
        WorkOrder order = workOrderService.getById(id);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        // 权限校验：政务人员和社区人员都可以查看所有工单（或者根据业务需要限制）
        // 如果你需要限制社区人员只能查看特定类型的工单，可以在这里添加逻辑
        return convertToVO(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processWorkOrder(WorkOrderProcessRequest request, User loginUser) {
        WorkOrder order = workOrderService.getById(request.getId());
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        // 权限校验：政务人员和社区人员都可以处理工单
        // 如果你需要限制处理权限，可以在这里添加逻辑

        // 更新工单状态和处理结果
        order.setStatus(request.getStatus());
        order.setResult(request.getResult());
        order.setHandlerId(loginUser.getId());
        order.setHandlerName(loginUser.getUserName());
        order.setHandleTime(new Date());
        boolean updated = workOrderService.updateById(order);
        if (updated) {
            saveOrderLog(order.getId(), loginUser.getId(), "process",
                    order.getStatus(), request.getStatus(), "处理工单");
        }
        return updated;
    }
    // ============== 私有方法 ==============

    private String generateOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateStr = sdf.format(new Date());
        int random = (int) (Math.random() * 900) + 100;
        return "WO" + dateStr + random;
    }

    private String getProofTitle(String subType) {
        Map<String, String> titleMap = new HashMap<>();
        titleMap.put("residence", "入学居住证明申请");
        titleMap.put("economic", "家庭经济状况证明申请");
        titleMap.put("activity", "社区活动证明申请");
        titleMap.put("background", "政审证明申请");
        return titleMap.getOrDefault(subType, "社区证明申请");
    }

    private String getHelpTitle(String subType) {
        Map<String, String> titleMap = new HashMap<>();
        titleMap.put("repair", "物业报修申请");
        titleMap.put("consult", "政策咨询");
        titleMap.put("elderly", "老人关怀申请");
        titleMap.put("other", "其他帮助申请");
        return titleMap.getOrDefault(subType, "社区帮助申请");
    }

    private void saveOrderLog(Integer orderId, Long userId, String action,
                              Integer fromStatus, Integer toStatus, String remark) {
        WorkOrderLog log = new WorkOrderLog();
        log.setOrderId(orderId);
        log.setOperatorId(userId);
        log.setAction(action);
        log.setFromStatus(fromStatus);
        log.setToStatus(toStatus);
        log.setRemark(remark);
        workOrderLogService.save(log);
    }

    // 转换方法
    private GovServiceApplicationVO convertToGovVO(ServiceApplication application) {
        GovServiceApplicationVO vo = new GovServiceApplicationVO();
        BeanUtils.copyProperties(application, vo);

        // 设置状态名称
        switch (application.getStatus()) {
            case 1:
                vo.setStatusText("已提交");
                break;
            case 2:
                vo.setStatusText("审核中");
                break;
            case 3:
                vo.setStatusText("已完成");
                break;
            case 4:
                vo.setStatusText("已驳回");
                break;
            default:
                vo.setStatusText("未知");
        }

        // 查询服务名称和分类
        if (application.getServiceId() != null) {
            ServiceItem serviceItem = serviceItemService.getById(application.getServiceId());
            if (serviceItem != null) {
                vo.setServiceName(serviceItem.getServiceName());
                vo.setServiceType(serviceItem.getServiceCode());

                // 查询分类名称
                if (serviceItem.getCategoryId() != null) {
                    InfoCategory category = infoCategoryService.getById(serviceItem.getCategoryId());
                    if (category != null) {
                        vo.setCategoryName(category.getCategoryName());
                    }
                }
            }
        }

        return vo;
    }


    private WorkOrderVO convertToVO(WorkOrder order) {
        if (order == null) {
            return null;
        }

        WorkOrderVO vo = new WorkOrderVO();
        BeanUtils.copyProperties(order, vo);

        // 处理JSON字段
        if (StrUtil.isNotBlank(order.getFormData())) {
            vo.setFormData(JSONUtil.parseObj(order.getFormData()));
        }

        if (StrUtil.isNotBlank(order.getAttachments())) {
            vo.setAttachments(JSONUtil.toList(order.getAttachments(), String.class));
        }

        if (StrUtil.isNotBlank(order.getResultFiles())) {
            vo.setResultFiles(JSONUtil.toList(order.getResultFiles(), String.class));
        }

        // 设置类型名称
        switch (order.getType()) {
            case 1:
                vo.setTypeName("社区证明");
                break;
            case 2:
                vo.setTypeName("社区帮助");
                break;
            case 3:
                vo.setTypeName("投诉建议");
                break;
        }

        // 设置状态名称
        switch (order.getStatus()) {
            case 1:
                vo.setStatusText("待受理");
                break;
            case 2:
                vo.setStatusText("处理中");
                break;
            case 3:
                vo.setStatusText("已完成");
                break;
            case 4:
                vo.setStatusText("已驳回");
                break;
        }

        // 设置子类型名称
        if (order.getType() == 1) {
            Map<String, String> subTypeMap = new HashMap<>();
            subTypeMap.put("residence", "居住证明");
            subTypeMap.put("economic", "经济状况证明");
            subTypeMap.put("activity", "活动证明");
            subTypeMap.put("background", "政审证明");
            vo.setSubTypeName(subTypeMap.get(order.getSubType()));
        } else if (order.getType() == 2) {
            Map<String, String> subTypeMap = new HashMap<>();
            subTypeMap.put("repair", "物业报修");
            subTypeMap.put("consult", "政策咨询");
            subTypeMap.put("elderly", "老人关怀");
            subTypeMap.put("other", "其他");
            vo.setSubTypeName(subTypeMap.get(order.getSubType()));
        } else if (order.getType() == 3) {
            Map<String, String> subTypeMap = new HashMap<>();
            subTypeMap.put("environmental", "环境卫生");
            subTypeMap.put("management", "社区管理");
            subTypeMap.put("facility", "设施维护");
            subTypeMap.put("other", "其他");
            vo.setSubTypeName(subTypeMap.get(order.getSubType()));
        }

        return vo;
    }

}