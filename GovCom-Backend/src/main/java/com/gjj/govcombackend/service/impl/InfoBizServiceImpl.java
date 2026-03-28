// InfoBizServiceImpl.java
package com.gjj.govcombackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.govcombackend.exception.BusinessException;
import com.gjj.govcombackend.exception.ErrorCode;
import com.gjj.govcombackend.model.entity.InfoArticle;
import com.gjj.govcombackend.model.entity.InfoCategory;
import com.gjj.govcombackend.model.entity.UserCommunity;
import com.gjj.govcombackend.model.vo.InfoArticleVO;
import com.gjj.govcombackend.service.InfoBizService;
import com.gjj.govcombackend.service.InfoArticleService; // Mybatis-X生成的
import com.gjj.govcombackend.service.InfoCategoryService; // Mybatis-X生成的
import com.gjj.govcombackend.service.UserCommunityService; // Mybatis-X生成的
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InfoBizServiceImpl implements InfoBizService {

    @Resource
    private InfoArticleService articleService;

    @Resource
    private InfoCategoryService categoryService;

    @Resource
    private UserCommunityService userCommunityService;

    @Override
    public List<InfoCategory> getCategoryList() {
        QueryWrapper<InfoCategory> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1)
                .orderByAsc("sortOrder");
        return categoryService.list(wrapper);
    }

    // InfoBizServiceImpl.java
    @Override
    public List<InfoArticleVO> getArticleList(Integer categoryId, Integer pageNum, Integer pageSize, Long userId, Integer scope) {
        Page<InfoArticle> page = new Page<>(pageNum, pageSize);
        QueryWrapper<InfoArticle> wrapper = new QueryWrapper<>();

        // 1. 状态条件
        wrapper.eq("status", 1);

        // 2. 分类条件
        if (categoryId != null) {
            wrapper.eq("categoryId", categoryId);
        }

        // 3. 范围条件（新增）
        if (scope != null) {
            wrapper.eq("scope", scope);
        }

        // 4. 根据登录状态决定可见范围（如果scope已经指定，可以简化）
        if (userId != null && scope == null) {
            // 已登录且没有指定scope：可查看政务资讯 + 自己社区的资讯
            List<Integer> userCommunityIds = getUserCommunityIds(userId);
            if (!userCommunityIds.isEmpty()) {
                wrapper.and(w ->
                        w.eq("scope", 1)
                                .or(q -> q.eq("scope", 2).in("communityId", userCommunityIds))
                );
            } else {
                wrapper.eq("scope", 1);
            }
        } else if (userId == null && scope == null) {
            // 未登录且没有指定scope：只能看政务资讯
            wrapper.eq("scope", 1);
        }
        // 如果指定了scope，上面的条件就不需要了

        // 5. 排序
        wrapper.orderByDesc("isTop", "publishTime");

        Page<InfoArticle> result = articleService.page(page, wrapper);

        return result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public InfoArticleVO getArticleDetail(Integer id, Long userId) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文章ID不能为空");
        }

        // 1. 查询文章
        InfoArticle article = articleService.getById(id);
        if (article == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "文章不存在");
        }

        // 2. 权限校验（如果是社区资讯，检查用户是否有权限）
        if (article.getScope() == 2 && article.getCommunityId() != null) {
            if (userId == null) {
                throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "请先登录查看社区资讯");
            }

            List<Integer> userCommunityIds = getUserCommunityIds(userId);
            if (!userCommunityIds.contains(article.getCommunityId())) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "您无权查看该社区资讯");
            }
        }

        // 3. 增加阅读量
        article.setViewCount(article.getViewCount() + 1);
        articleService.updateById(article);

        return convertToVO(article);
    }

    @Override
    public List<InfoArticleVO> searchArticle(String keyword, Long userId) {
        if (!StringUtils.hasText(keyword)) {
            return Collections.emptyList();
        }

        QueryWrapper<InfoArticle> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1)
                .and(w -> w.like("title", keyword).or().like("summary", keyword));

        // 根据登录状态决定可见范围
        if (userId != null) {
            List<Integer> userCommunityIds = getUserCommunityIds(userId);
            if (!userCommunityIds.isEmpty()) {
                wrapper.and(w ->
                        w.eq("scope", 1)
                                .or(q -> q.eq("scope", 2).in("communityId", userCommunityIds))
                );
            } else {
                wrapper.eq("scope", 1);
            }
        } else {
            wrapper.eq("scope", 1);
        }

        wrapper.orderByDesc("publishTime")
                .last("LIMIT 20"); // 最多返回20条

        List<InfoArticle> list = articleService.list(wrapper);

        return list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InfoArticleVO> getBannerArticles() {
        QueryWrapper<InfoArticle> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1)
                .eq("isTop", 1)
                .orderByDesc("publishTime")
                .last("LIMIT 5"); // 取5条轮播

        List<InfoArticle> list = articleService.list(wrapper);

        return list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InfoArticleVO> getCommunityArticles(Integer communityId, Long userId, Integer categoryId, Integer pageNum, Integer pageSize) {
        if (communityId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "社区ID不能为空");
        }

        // 权限校验：必须登录且是该社区成员
        if (userId == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        }

        List<Integer> userCommunityIds = getUserCommunityIds(userId);
        if (!userCommunityIds.contains(communityId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "您不是该社区成员");
        }

        Page<InfoArticle> page = new Page<>(pageNum, pageSize);
        QueryWrapper<InfoArticle> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1)
                .eq("scope", 2)
                .eq("communityId", communityId);

        // ✅ 如果传了 categoryId，加上分类过滤
        if (categoryId != null) {
            wrapper.eq("categoryId", categoryId);
        }

        wrapper.orderByDesc("isTop", "publishTime");

        Page<InfoArticle> result = articleService.page(page, wrapper);

        return result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    // ============== 私有工具方法 ==============

    /**
     * 获取用户加入的社区ID列表
     */
    private List<Integer> getUserCommunityIds(Long userId) {
        QueryWrapper<UserCommunity> wrapper = new QueryWrapper<>();
        wrapper.eq("userId", userId)
                .eq("status", 1);

        List<UserCommunity> list = userCommunityService.list(wrapper);
        return list.stream()
                .map(UserCommunity::getCommunityId)
                .collect(Collectors.toList());
    }

    /**
     * 将InfoArticle转换为VO
     */
    private InfoArticleVO convertToVO(InfoArticle article) {
        if (article == null) {
            return null;
        }

        InfoArticleVO vo = new InfoArticleVO();
        BeanUtils.copyProperties(article, vo);

        // 查询分类名称
        if (article.getCategoryId() != null) {
            InfoCategory category = categoryService.getById(article.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getCategoryName());
            }
        }

        // 如果是社区资讯，可以查询社区名称（这里简化，不查community表了）
        if (article.getScope() == 2 && article.getCommunityId() != null) {
            // 可以根据communityId查询社区名称，暂时用ID代替
            vo.setCommunityName("社区" + article.getCommunityId());
        }

        return vo;
    }
}