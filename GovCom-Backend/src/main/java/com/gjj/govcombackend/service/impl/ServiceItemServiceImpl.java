package com.gjj.govcombackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjj.govcombackend.model.entity.ServiceItem;
import com.gjj.govcombackend.service.ServiceItemService;
import com.gjj.govcombackend.mapper.ServiceItemMapper;
import org.springframework.stereotype.Service;

/**
* @author 78568
* @description 针对表【service_item(服务事项表)】的数据库操作Service实现
* @createDate 2026-03-12 19:21:54
*/
@Service
public class ServiceItemServiceImpl extends ServiceImpl<ServiceItemMapper, ServiceItem>
    implements ServiceItemService {

}




