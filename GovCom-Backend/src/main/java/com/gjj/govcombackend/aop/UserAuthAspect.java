package com.gjj.govcombackend.aop;

import com.gjj.govcombackend.annotion.UserAuthCheck;
import com.gjj.govcombackend.exception.ErrorCode;
import com.gjj.govcombackend.exception.ThrowUtils;
import com.gjj.govcombackend.model.entity.User;
import com.gjj.govcombackend.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.gjj.govcombackend.model.constant.UserConstant.USER_LOGIN_STATE;

@Aspect
@Component
public class UserAuthAspect {

    @Resource
    private UserService userService;

    @Around("@annotation(userAuthCheck)")
    public Object around(ProceedingJoinPoint joinPoint , UserAuthCheck userAuthCheck) throws Throwable {
        // 1. 获取当前请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        ThrowUtils.throwIf(attributes == null , ErrorCode.PARAMS_ERROR , "无法获取当前请求");
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession(false);

        // 2. 检查登录状态
        if (userAuthCheck.checkLogin()) {
            // 检查会话是否存在
            ThrowUtils.throwIf(session == null , ErrorCode.NOT_LOGIN_ERROR);

            User sessionUser = (User) session.getAttribute(USER_LOGIN_STATE);
            ThrowUtils.throwIf(sessionUser == null , ErrorCode.NOT_LOGIN_ERROR);

            // 从数据库获取最新用户信息
            User currentUser = userService.getById(sessionUser.getId());
            ThrowUtils.throwIf(currentUser == null , ErrorCode.NOT_LOGIN_ERROR , "用户不存在");

            // 更新 Session 中的用户信息（可选，保持最新）
            session.setAttribute(USER_LOGIN_STATE , currentUser);

            // 3. 检查用户状态
            if (userAuthCheck.checkStatus()) {
                // 检查用户是否被禁用（假设 status = 1 表示禁用）
                ThrowUtils.throwIf(currentUser.getStatus() == 1 , ErrorCode.FORBIDDEN_ERROR , "账号已被禁用");

                // 检查逻辑删除状态
                ThrowUtils.throwIf(currentUser.getIsDeleted() == 1 , ErrorCode.NO_AUTH_ERROR , "用户已被删除");
            }
        }

        // 4. 执行目标方法
        return joinPoint.proceed();
    }
}