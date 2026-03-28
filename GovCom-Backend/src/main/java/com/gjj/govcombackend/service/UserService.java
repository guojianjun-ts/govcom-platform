package com.gjj.govcombackend.service;

import com.gjj.govcombackend.model.dto.user.UserUpdateRequest;
import com.gjj.govcombackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gjj.govcombackend.model.vo.LoginUserVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 78568
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2026-03-05 17:46:41
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request httpRequest 请求方便设置 cookie
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @return 脱敏的已登录用户信息
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取当前登录用户
     *
     * @param request request
     * @return 当前登录用户
     */
    User getLoginUser(HttpServletRequest request);


    /**
     * 更新当前登录用户信息
     *
     * @param userUpdateRequest 用户更新请求
     * @param request           request
     * @return 是否更新成功
     */
    boolean updateCurrentUser(UserUpdateRequest userUpdateRequest , HttpServletRequest request);

    /**
     * 修改密码
     *
     * @param oldPassword    原密码
     * @param newPassword    新密码
     * @param checkPassword  确认密码
     * @param request        request
     * @return 是否修改成功
     */
    boolean updatePassword(String oldPassword, String newPassword, String checkPassword,
                           HttpServletRequest request);

    /**
     * 获取加密后的密码
     *
     * @param userPassword 原始密码
     * @return 加密后的密码
     */
    String getEncryptPassword(String userPassword);

}
