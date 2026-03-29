// WorkOrderBizService.java
package com.gjj.govcombackend.service;

import com.gjj.govcombackend.model.dto.life.ProofApplyRequest;
import com.gjj.govcombackend.model.dto.life.HelpApplyRequest;
import com.gjj.govcombackend.model.dto.life.ComplaintApplyRequest;
import com.gjj.govcombackend.model.dto.life.WorkOrderQueryRequest;
import com.gjj.govcombackend.model.dto.workOrder.GovWorkOrderProcessRequest;
import com.gjj.govcombackend.model.dto.workOrder.WorkOrderProcessRequest;
import com.gjj.govcombackend.model.entity.User;
import com.gjj.govcombackend.model.vo.GovServiceApplicationVO;
import com.gjj.govcombackend.model.vo.WorkOrderVO;
import java.util.List;

public interface WorkOrderBizService {

    /**
     * 提交社区证明申请
     */
    Integer submitProof(ProofApplyRequest request, Long userId);

    /**
     * 提交社区帮助申请
     */
    Integer submitHelp(HelpApplyRequest request, Long userId);

    /**
     * 提交投诉建议
     */
    Integer submitComplaint(ComplaintApplyRequest request, Long userId);

    /**
     * 获取用户的申请列表
     */
    List<WorkOrderVO> getUserOrders(WorkOrderQueryRequest request, Long userId);


    /**
     * 获取政务工单详情
     */
    GovServiceApplicationVO getGovWorkOrderDetail(Integer id);


    /**
     * 获取申请详情
     */
    WorkOrderVO getOrderDetail(Integer id, Long userId);


    // 在 WorkOrderBizService.java 中添加
    List<WorkOrderVO> getGovWorkOrders(WorkOrderQueryRequest request, User loginUser);

    /**
     * 获取政务工单列表
     */
    List<GovServiceApplicationVO> getGovWorkOrders(WorkOrderQueryRequest request);

    /**
     * 处理政务工单
     */
    boolean processGovWorkOrder(GovWorkOrderProcessRequest request);

    List<WorkOrderVO> getCommunityWorkOrders(WorkOrderQueryRequest request, User loginUser);

    WorkOrderVO getWorkOrderDetail(Integer id, User loginUser);

    boolean processWorkOrder(WorkOrderProcessRequest request, User loginUser);


}