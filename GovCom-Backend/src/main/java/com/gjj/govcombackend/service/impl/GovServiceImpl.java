package com.gjj.govcombackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.govcombackend.exception.BusinessException;
import com.gjj.govcombackend.exception.ErrorCode;
import com.gjj.govcombackend.model.entity.GovServiceCategory;
import com.gjj.govcombackend.model.entity.GovServiceItem;
import com.gjj.govcombackend.model.entity.GovServiceApplication;
import com.gjj.govcombackend.model.dto.govservice.ServiceApplicationRequest;
import com.gjj.govcombackend.model.vo.ServiceItemVO;
import com.gjj.govcombackend.model.vo.ServiceApplicationVO;
import com.gjj.govcombackend.service.GovService;
import com.gjj.govcombackend.service.ServiceCategoryService;
import com.gjj.govcombackend.service.ServiceItemService;
import com.gjj.govcombackend.service.ServiceApplicationService;
import cn.hutool.json.JSONUtil;
import cn.hutool.core.util.StrUtil;
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
public class GovServiceImpl implements GovService {

    @Resource
    private ServiceCategoryService categoryService;

    @Resource
    private ServiceItemService itemService;

    @Resource
    private ServiceApplicationService applicationService;

    // ============== 分类相关 ==============

    @Override
    public List<GovServiceCategory> getCategoryList() {
        QueryWrapper<GovServiceCategory> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1)
                .orderByAsc("sortOrder");
        return categoryService.list(wrapper);
    }

    // ============== 服务事项相关 ==============

    @Override
    public List<ServiceItemVO> getServiceList(Integer categoryId, Integer pageNum, Integer pageSize) {
        if (categoryId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分类ID不能为空");
        }

        Page<GovServiceItem> page = new Page<>(pageNum, pageSize);
        QueryWrapper<GovServiceItem> wrapper = new QueryWrapper<>();
        wrapper.eq("categoryId", categoryId)
                .eq("status", 1)
                .orderByDesc("applyCount");

        Page<GovServiceItem> result = itemService.page(page, wrapper);

        return result.getRecords().stream()
                .map(this::convertToItemVO)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceItemVO getServiceDetail(Integer id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "服务ID不能为空");
        }

        GovServiceItem item = itemService.getById(id);
        if (item == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "服务不存在");
        }

        // 增加浏览次数
        item.setViewCount(item.getViewCount() == null ? 1 : item.getViewCount() + 1);
        itemService.updateById(item);

        return convertToItemVO(item);
    }

    @Override
    public List<ServiceItemVO> searchService(String keyword) {
        if (StrUtil.isBlank(keyword)) {
            return new ArrayList<>();
        }

        QueryWrapper<GovServiceItem> wrapper = new QueryWrapper<>();
        wrapper.like("serviceName", keyword)
                .or()
                .like("briefDesc", keyword)
                .eq("status", 1)
                .orderByDesc("applyCount");

        List<GovServiceItem> list = itemService.list(wrapper);

        return list.stream()
                .map(this::convertToItemVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceItemVO> getHotServices() {
        QueryWrapper<GovServiceItem> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1)
                .orderByDesc("applyCount", "viewCount")
                .last("LIMIT 8");

        List<GovServiceItem> list = itemService.list(wrapper);

        return list.stream()
                .map(this::convertToItemVO)
                .collect(Collectors.toList());
    }

    // ============== 申请相关 ==============

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitApplication(ServiceApplicationRequest request, Long userId) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        }
        if (request.getServiceId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "服务ID不能为空");
        }
        if (userId == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录");
        }

        // 检查服务是否存在
        GovServiceItem govServiceItem = itemService.getById(request.getServiceId());
        if (govServiceItem == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "服务不存在");
        }

        // 生成申请单号
        String applicationNo = generateApplicationNo();

        // 创建申请记录
        GovServiceApplication application = new GovServiceApplication();
        BeanUtils.copyProperties(request, application);
        application.setApplicationNo(applicationNo);
        application.setUserId(userId);
        application.setStatus(1); // 1-已提交
        application.setSubmitTime(new Date());
        // 注意：remark 字段已移除

        boolean saved = applicationService.save(application);

        if (saved) {
            govServiceItem.setApplyCount(govServiceItem.getApplyCount() == null ?
                    1 : govServiceItem.getApplyCount() + 1);
            itemService.updateById(govServiceItem);
            log.info("服务申请成功 - 申请单号: {}, 用户ID: {}", applicationNo, userId);
        }

        return saved;
    }

    @Override
    public List<ServiceApplicationVO> getMyApplications(Long userId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录");
        }

        QueryWrapper<GovServiceApplication> wrapper = new QueryWrapper<>();
        wrapper.eq("userId", userId)
                .orderByDesc("createTime");

        List<GovServiceApplication> list = applicationService.list(wrapper);

        return list.stream()
                .map(this::convertToApplicationVO)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceApplicationVO getApplicationDetail(Integer id, Long userId) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "申请ID不能为空");
        }
        if (userId == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录");
        }

        GovServiceApplication app = applicationService.getById(id);
        if (app == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "申请记录不存在");
        }

        // 权限校验
        if (!app.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权查看他人申请");
        }

        return convertToApplicationVO(app);
    }

    // ============== 私有转换方法 ==============

    /**
     * 将ServiceItem转换为VO
     */
    private ServiceItemVO convertToItemVO(GovServiceItem item) {
        if (item == null) {
            return null;
        }

        ServiceItemVO vo = new ServiceItemVO();
        BeanUtils.copyProperties(item, vo);

        // 使用Hutool的JSONUtil处理JSON字符串
        if (StrUtil.isNotBlank(item.getMaterialList())) {
            vo.setMaterialList(JSONUtil.toList(item.getMaterialList(), String.class));
        } else {
            vo.setMaterialList(new ArrayList<>());
        }

        if (StrUtil.isNotBlank(item.getBaseInfo())) {
            vo.setBaseInfo(JSONUtil.toBean(item.getBaseInfo(), Map.class));
        } else {
            vo.setBaseInfo(new HashMap<>());
        }

        return vo;
    }

    /**
     * 将ServiceApplication转换为VO
     */
    private ServiceApplicationVO convertToApplicationVO(GovServiceApplication application) {
        if (application == null) {
            return null;
        }

        ServiceApplicationVO vo = new ServiceApplicationVO();
        BeanUtils.copyProperties(application, vo);

        // 状态中文描述
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

        // 查询服务名称和图标
        if (application.getServiceId() != null) {
            GovServiceItem item = itemService.getById(application.getServiceId());
            if (item != null) {
                vo.setServiceName(item.getServiceName());
                vo.setServiceIcon(item.getIcon());
            }
        }

        return vo;
    }

    // ============== 私有工具方法 ==============

    /**
     * 生成申请单号
     */
    private String generateApplicationNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateStr = sdf.format(new Date());
        int random = (int) (Math.random() * 900) + 100;
        return "APP" + dateStr + random;
    }
}