package com.gjj.govcombackend.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gjj.govcombackend.exception.BusinessException;
import com.gjj.govcombackend.exception.ErrorCode;
import com.gjj.govcombackend.model.dto.user.UserUpdateRequest;
import com.gjj.govcombackend.model.entity.User;
import com.gjj.govcombackend.model.vo.LoginUserVO;
import com.gjj.govcombackend.service.UserService;
import com.gjj.govcombackend.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.gjj.govcombackend.model.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author 78568
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2026-03-05 17:46:41
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StrUtil.isAllBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        // 2. 检查是否重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }
        // 3. 加密
        String encryptPassword = getEncryptPassword(userPassword);
        // 4. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName("无名");

        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }
        return user.getId();
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StrUtil.isAllBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = getEncryptPassword(userPassword);
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 3. 记录用户的登录态
        HttpSession session = request.getSession();
        session.setAttribute(USER_LOGIN_STATE, user);

        // 添加调试日志
        log.info("用户登录成功 - Session ID: {}, 用户ID: {}, 用户账号: {}",
                session.getId(), user.getId(), user.getUserAccount());

        return this.getLoginUserVO(user);
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }


    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }


    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接返回上述结果）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    @Override
    public boolean updateCurrentUser(UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        // 1. 获取当前登录用户
        User loginUser = this.getLoginUser(request);

        // 2. 参数校验
        if (userUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不能为空");
        }

        // 3. 创建更新对象
        User updateUser = new User();
        updateUser.setId(loginUser.getId());

        // 4. 只更新有值的字段
        if (StrUtil.isNotBlank(userUpdateRequest.getUserName())) {
            if (userUpdateRequest.getUserName().length() > 20) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "昵称过长，最大20个字符");
            }
            updateUser.setUserName(userUpdateRequest.getUserName());
        }

        if (userUpdateRequest.getGender() != null) {
            if (userUpdateRequest.getGender() != 0 && userUpdateRequest.getGender() != 1) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "性别参数错误");
            }
            updateUser.setGender(userUpdateRequest.getGender());
        }

        // ✅ 修改这里：只在手机号不为空时才校验
        if (StrUtil.isNotBlank(userUpdateRequest.getPhone())) {
            // 手机号格式校验
            if (!userUpdateRequest.getPhone().matches("^1[3-9]\\d{9}$")) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号格式不正确");
            }
            updateUser.setPhone(userUpdateRequest.getPhone());
        }

        if (StrUtil.isNotBlank(userUpdateRequest.getUserAvatar())) {
            if (userUpdateRequest.getUserAvatar().length() > 500) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "头像URL过长");
            }
            updateUser.setUserAvatar(userUpdateRequest.getUserAvatar());
        }

        // 5. 执行更新
        boolean result = this.updateById(updateUser);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }

        // 6. 更新Session中的用户信息
        User updatedUser = this.getById(loginUser.getId());
        request.getSession().setAttribute(USER_LOGIN_STATE, updatedUser);

        log.info("用户信息更新成功 - 用户ID: {}", loginUser.getId());

        return true;
    }


    @Override
    public boolean updatePassword(String oldPassword, String newPassword, String checkPassword,
                                  HttpServletRequest request) {
        // 1. 获取当前登录用户
        User loginUser = this.getLoginUser(request);

        // 2. 参数校验
        if (StrUtil.isAllBlank(oldPassword, newPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }

        if (newPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "新密码长度不能小于8位");
        }

        if (!newPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的新密码不一致");
        }

        // 3. 验证原密码是否正确
        String encryptOldPassword = getEncryptPassword(oldPassword);
        if (!encryptOldPassword.equals(loginUser.getUserPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "原密码错误");
        }

        // 4. 更新密码
        String encryptNewPassword = getEncryptPassword(newPassword);
        User updateUser = new User();
        updateUser.setId(loginUser.getId());
        updateUser.setUserPassword(encryptNewPassword);

        boolean result = this.updateById(updateUser);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "密码修改失败");
        }

        // 5. 可选：修改密码后强制退出登录（让用户重新登录）
        // this.userLogout(request);

        log.info("密码修改成功 - 用户ID: {}", loginUser.getId());

        return true;
    }


    @Override
    public String getEncryptPassword(String userPassword) {
        // 盐值，混淆密码
        final String SALT = "nmcSalt";
        return DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    }
}




