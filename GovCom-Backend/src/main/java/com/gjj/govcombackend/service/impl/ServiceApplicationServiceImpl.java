package com.gjj.govcombackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjj.govcombackend.model.entity.ServiceApplication;
import com.gjj.govcombackend.service.ServiceApplicationService;
import com.gjj.govcombackend.mapper.ServiceApplicationMapper;
import org.springframework.stereotype.Service;

/**
* @author 78568
* @description 针对表【service_application(服务申请表)】的数据库操作Service实现
* @createDate 2026-03-12 19:21:49
*/
@Service
public class ServiceApplicationServiceImpl extends ServiceImpl<ServiceApplicationMapper, ServiceApplication>
    implements ServiceApplicationService {

}




