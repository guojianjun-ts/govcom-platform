// CommunityPostBizService.java
package com.gjj.govcombackend.service;

import com.gjj.govcombackend.model.dto.community.PostCreateRequest;
import com.gjj.govcombackend.model.vo.CommunityPostVO;
import com.gjj.govcombackend.model.vo.CommunityCommentVO;
import java.util.List;

public interface CommunityPostBizService {

    /**
     * 获取帖子列表
     * @param communityId 社区ID（可选）
     * @param type 类型（可选）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param userId 当前用户ID（用于判断是否点赞）
     */
    List<CommunityPostVO> getPostList(Integer communityId, Integer type, Integer pageNum, Integer pageSize, Long userId);

    /**
     * 获取帖子详情
     */
    CommunityPostVO getPostDetail(Integer postId, Long userId);

    /**
     * 发布帖子
     */
    Integer createPost(PostCreateRequest request, Long userId);

    /**
     * 获取评论列表
     */
    List<CommunityCommentVO> getComments(Integer postId, Long userId);

    /**
     * 发表评论
     */
    Integer createComment(Integer postId, String content, Long userId, Integer parentId);

    /**
     * 点赞/取消点赞
     */
    boolean toggleLike(Integer postId, Integer commentId, Long userId);
}