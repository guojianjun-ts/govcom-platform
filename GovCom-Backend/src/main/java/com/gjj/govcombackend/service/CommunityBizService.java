// CommunityBizService.java
package com.gjj.govcombackend.service;

import com.gjj.govcombackend.model.entity.Community;
import com.gjj.govcombackend.model.vo.CommunityVO;
import java.util.List;

public interface CommunityBizService {

    /**
     * 获取所有社区列表
     */
    List<CommunityVO> getCommunityList();

    /**
     * 获取当前用户加入的社区列表
     */
    List<CommunityVO> getMyCommunities(Long userId);

    /**
     * 获取社区详情
     */
    Community getCommunityDetail(Integer id);

    /**
     * 用户加入社区
     */
    boolean joinCommunity(Long userId, Integer communityId);

    /**
     * 用户退出社区
     */
    boolean leaveCommunity(Long userId, Integer communityId);
}