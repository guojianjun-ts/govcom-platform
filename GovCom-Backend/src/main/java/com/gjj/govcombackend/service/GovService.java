package com.gjj.govcombackend.service;

import com.gjj.govcombackend.model.entity.ServiceCategory;
import com.gjj.govcombackend.model.entity.ServiceApplication;
import com.gjj.govcombackend.model.dto.govservice.ServiceApplicationRequest;
import com.gjj.govcombackend.model.vo.ServiceItemVO;
import com.gjj.govcombackend.model.vo.ServiceApplicationVO;

import java.util.List;

public interface GovService {

    // ============== 分类相关 ==============
    /**
     * 获取所有服务分类
     */
    List<ServiceCategory> getCategoryList();

    // ============== 服务事项相关 ==============
    /**
     * 获取分类下的服务列表
     */
    List<ServiceItemVO> getServiceList(Integer categoryId, Integer pageNum, Integer pageSize);

    /**
     * 获取服务详情
     */
    ServiceItemVO getServiceDetail(Integer id);

    /**
     * 搜索服务
     */
    List<ServiceItemVO> searchService(String keyword);

    /**
     * 获取热门服务
     */
    List<ServiceItemVO> getHotServices();

    // ============== 申请相关 ==============
    /**
     * 提交服务申请
     */
    boolean submitApplication(ServiceApplicationRequest request, Long userId);

    /**
     * 获取我的申请列表
     */
    List<ServiceApplicationVO> getMyApplications(Long userId);

    /**
     * 获取申请详情
     */
    ServiceApplicationVO getApplicationDetail(Integer id, Long userId);
}