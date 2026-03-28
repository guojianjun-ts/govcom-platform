// CommunityBizServiceImpl.java
package com.gjj.govcombackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gjj.govcombackend.exception.BusinessException;
import com.gjj.govcombackend.exception.ErrorCode;
import com.gjj.govcombackend.model.entity.Community;
import com.gjj.govcombackend.model.entity.UserCommunity;
import com.gjj.govcombackend.model.vo.CommunityVO;
import com.gjj.govcombackend.service.CommunityBizService;
import com.gjj.govcombackend.service.CommunityService;
import com.gjj.govcombackend.service.UserCommunityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommunityBizServiceImpl implements CommunityBizService {

    @Resource
    private CommunityService communityService; // Mybatis-X生成的

    @Resource
    private UserCommunityService userCommunityService; // Mybatis-X生成的

    @Override
    public List<CommunityVO> getCommunityList() {
        QueryWrapper<Community> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1)
                .orderByAsc("id");

        List<Community> list = communityService.list(wrapper);

        return list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommunityVO> getMyCommunities(Long userId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录");
        }

        // 1. 查询用户加入的社区ID
        QueryWrapper<UserCommunity> userWrapper = new QueryWrapper<>();
        userWrapper.eq("userId", userId)
                .eq("status", 1);

        List<UserCommunity> userCommunities = userCommunityService.list(userWrapper);
        List<Integer> communityIds = userCommunities.stream()
                .map(UserCommunity::getCommunityId)
                .collect(Collectors.toList());

        if (communityIds.isEmpty()) {
            return Collections.emptyList(); // ✅ Java 8 兼容
        }

        // 2. 查询社区详情
        QueryWrapper<Community> communityWrapper = new QueryWrapper<>();
        communityWrapper.in("id", communityIds)
                .eq("status", 1);

        List<Community> communities = communityService.list(communityWrapper);

        // 3. 转换为VO，并标记已加入
        return communities.stream()
                .map(community -> {
                    CommunityVO vo = convertToVO(community);
                    vo.setIsJoined(true);
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Community getCommunityDetail(Integer id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "社区ID不能为空");
        }

        Community community = communityService.getById(id);
        if (community == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "社区不存在");
        }

        return community;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean joinCommunity(Long userId, Integer communityId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录");
        }
        if (communityId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "社区ID不能为空");
        }

        // 1. 检查社区是否存在
        Community community = communityService.getById(communityId);
        if (community == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "社区不存在");
        }

        // 2. 检查是否已经加入
        QueryWrapper<UserCommunity> wrapper = new QueryWrapper<>();
        wrapper.eq("userId", userId)
                .eq("communityId", communityId);

        long count = userCommunityService.count(wrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已经加入该社区");
        }

        // 3. 加入社区
        UserCommunity userCommunity = new UserCommunity();
        userCommunity.setUserId(userId);
        userCommunity.setCommunityId(communityId);
        userCommunity.setStatus(1);

        boolean saved = userCommunityService.save(userCommunity);

        if (saved) {
            log.info("用户加入社区成功 - userId: {}, communityId: {}", userId, communityId);
        }

        return saved;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean leaveCommunity(Long userId, Integer communityId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录");
        }
        if (communityId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "社区ID不能为空");
        }

        QueryWrapper<UserCommunity> wrapper = new QueryWrapper<>();
        wrapper.eq("userId", userId)
                .eq("communityId", communityId);

        boolean removed = userCommunityService.remove(wrapper);

        if (removed) {
            log.info("用户退出社区成功 - userId: {}, communityId: {}", userId, communityId);
        }

        return removed;
    }

    private CommunityVO convertToVO(Community community) {
        if (community == null) {
            return null;
        }

        CommunityVO vo = new CommunityVO();
        BeanUtils.copyProperties(community, vo);
        vo.setIsJoined(false);
        return vo;
    }
}