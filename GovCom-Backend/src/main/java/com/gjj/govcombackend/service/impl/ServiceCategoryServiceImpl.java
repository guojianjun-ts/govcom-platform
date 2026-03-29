package com.gjj.govcombackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjj.govcombackend.model.entity.GovServiceCategory;
import com.gjj.govcombackend.service.ServiceCategoryService;
import com.gjj.govcombackend.mapper.GovServiceCategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author 78568
* @description 针对表【service_category(服务分类表)】的数据库操作Service实现
* @createDate 2026-03-12 19:16:17
*/
@Service
public class ServiceCategoryServiceImpl extends ServiceImpl<GovServiceCategoryMapper, GovServiceCategory>
    implements ServiceCategoryService {

}




