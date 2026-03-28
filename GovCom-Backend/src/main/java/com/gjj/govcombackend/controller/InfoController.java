package com.gjj.govcombackend.controller;

import com.gjj.govcombackend.common.BaseResponse;
import com.gjj.govcombackend.common.ResultUtils;
import com.gjj.govcombackend.exception.ErrorCode;
import com.gjj.govcombackend.exception.ThrowUtils;
import com.gjj.govcombackend.model.entity.InfoCategory;
import com.gjj.govcombackend.model.entity.User;
import com.gjj.govcombackend.model.vo.InfoArticleVO;
import com.gjj.govcombackend.service.InfoBizService;
import com.gjj.govcombackend.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/info")
public class InfoController {

    @Resource
    private InfoBizService infoBizService;

    @Resource
    private UserService userService;

    // ============== 分类接口 ==============

    @GetMapping("/category/list")
    @ApiOperation("获取资讯分类列表")
    public BaseResponse<List<InfoCategory>> getInfoCategoryList() {
        return ResultUtils.success(infoBizService.getCategoryList());
    }

    // ============== 文章接口 ==============

    @GetMapping("/article/list")
    @ApiOperation("获取资讯列表（根据登录状态返回不同内容）")
    public BaseResponse<List<InfoArticleVO>> getArticleList(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer scope,  // ✅ 添加 scope 参数
            HttpServletRequest request) {

        // 尝试获取登录用户（可能为null）
        Long userId = null;
        try {
            User loginUser = userService.getLoginUser(request);
            userId = loginUser.getId();
        } catch (Exception e) {
            // 用户未登录，userId保持null
        }

        List<InfoArticleVO> list = infoBizService.getArticleList(categoryId, pageNum, pageSize, userId, scope);  // ✅ 传 scope
        return ResultUtils.success(list);
    }


    @GetMapping("/article/detail/{id}")
    @ApiOperation("获取资讯详情")
    public BaseResponse<InfoArticleVO> getArticleDetail(
            @PathVariable Integer id,
            HttpServletRequest request) {

        ThrowUtils.throwIf(id == null, ErrorCode.PARAMS_ERROR, "文章ID不能为空");

        // 尝试获取登录用户（可能为null）
        Long userId = null;
        try {
            User loginUser = userService.getLoginUser(request);
            userId = loginUser.getId();
        } catch (Exception e) {
            // 用户未登录，userId保持null
        }

        InfoArticleVO vo = infoBizService.getArticleDetail(id, userId);
        return ResultUtils.success(vo);
    }

    @GetMapping("/article/search")
    @ApiOperation("搜索资讯")
    public BaseResponse<List<InfoArticleVO>> searchArticle(
            @RequestParam String keyword,
            HttpServletRequest request) {

        ThrowUtils.throwIf(keyword == null || keyword.trim().isEmpty(),
                ErrorCode.PARAMS_ERROR, "搜索关键词不能为空");

        // 尝试获取登录用户（可能为null）
        Long userId = null;
        try {
            User loginUser = userService.getLoginUser(request);
            userId = loginUser.getId();
        } catch (Exception e) {
            // 用户未登录，userId保持null
        }

        List<InfoArticleVO> list = infoBizService.searchArticle(keyword, userId);
        return ResultUtils.success(list);
    }

    @GetMapping("/article/banner")
    @ApiOperation("获取轮播图资讯（置顶的前几条）")
    public BaseResponse<List<InfoArticleVO>> getBannerArticles() {
        return ResultUtils.success(infoBizService.getBannerArticles());
    }

// ============== 社区资讯接口 ==============

    @GetMapping("/community/{communityId}")
    @ApiOperation("获取指定社区的资讯（需是该社区成员）")
    public BaseResponse<List<InfoArticleVO>> getCommunityArticles(
            @PathVariable Integer communityId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer categoryId,  // ✅ 添加这个参数
            HttpServletRequest request) {

        ThrowUtils.throwIf(communityId == null, ErrorCode.PARAMS_ERROR, "社区ID不能为空");

        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);

        List<InfoArticleVO> list = infoBizService.getCommunityArticles(
                communityId, loginUser.getId(), categoryId, pageNum, pageSize);  // ✅ 传进去
        return ResultUtils.success(list);
    }

}