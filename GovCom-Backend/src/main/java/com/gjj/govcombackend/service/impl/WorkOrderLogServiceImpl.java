package com.gjj.govcombackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjj.govcombackend.model.entity.WorkOrderLog;
import com.gjj.govcombackend.service.WorkOrderLogService;
import com.gjj.govcombackend.mapper.WorkOrderLogMapper;
import org.springframework.stereotype.Service;

/**
* @author 78568
* @description 针对表【work_order_log(工单处理记录表)】的数据库操作Service实现
* @createDate 2026-03-15 13:06:59
*/
@Service
public class WorkOrderLogServiceImpl extends ServiceImpl<WorkOrderLogMapper, WorkOrderLog>
    implements WorkOrderLogService{

}




