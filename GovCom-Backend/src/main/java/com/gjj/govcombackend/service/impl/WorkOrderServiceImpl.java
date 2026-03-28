package com.gjj.govcombackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjj.govcombackend.model.entity.WorkOrder;
import com.gjj.govcombackend.service.WorkOrderService;
import com.gjj.govcombackend.mapper.WorkOrderMapper;
import org.springframework.stereotype.Service;

/**
* @author 78568
* @description 针对表【work_order(工单主表)】的数据库操作Service实现
* @createDate 2026-03-15 13:06:58
*/
@Service
public class WorkOrderServiceImpl extends ServiceImpl<WorkOrderMapper, WorkOrder>
    implements WorkOrderService {

}




