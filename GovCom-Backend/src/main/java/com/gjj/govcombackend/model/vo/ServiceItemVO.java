package com.gjj.govcombackend.model.vo;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class ServiceItemVO {
    // 基本信息
    private Integer id;
    private Integer categoryId;
    private String serviceName;
    private String serviceCode;
    private String briefDesc;
    private String icon;

    // 解析后的复杂字段
    private List<String> materialList;      // 材料列表（JSON数组转List）
    private String processDesc;              // 流程描述（普通文本）
    private Map<String, Object> baseInfo;    // 基本信息（JSON对象转Map）

    // 统计数据
    private Integer viewCount;
    private Integer applyCount;
}