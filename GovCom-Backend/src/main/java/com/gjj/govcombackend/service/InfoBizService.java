// InfoBizService.java
package com.gjj.govcombackend.service;

import com.gjj.govcombackend.model.entity.InfoCategory;
import com.gjj.govcombackend.model.vo.InfoArticleVO;
import java.util.List;

public interface InfoBizService {

    /**
     * 获取资讯分类列表
     */
    List<InfoCategory> getCategoryList();

    /**
     * 获取资讯列表（根据用户登录状态返回不同内容）
     * @param categoryId 分类ID（可选）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param userId 用户ID（可能为null，表示未登录）
     * @param scope 范围：1-政务，2-社区（可选）
     */
    List<InfoArticleVO> getArticleList(Integer categoryId, Integer pageNum, Integer pageSize, Long userId, Integer scope);

    /**
     * 获取资讯详情
     * @param id 文章ID
     * @param userId 用户ID（用于记录阅读量）
     */
    InfoArticleVO getArticleDetail(Integer id, Long userId);

    /**
     * 搜索资讯
     * @param keyword 关键词
     * @param userId 用户ID
     */
    List<InfoArticleVO> searchArticle(String keyword, Long userId);

    /**
     * 获取轮播图资讯（置顶的前几条）
     */
    List<InfoArticleVO> getBannerArticles();

    /**
     * 获取社区资讯
     * @param communityId 社区ID
     * @param userId 用户ID
     * @param categoryId 分类ID（可选）
     * @param pageNum 页码
     * @param pageSize 每页大小
     */
    List<InfoArticleVO> getCommunityArticles(Integer communityId, Long userId, Integer categoryId, Integer pageNum, Integer pageSize);
}