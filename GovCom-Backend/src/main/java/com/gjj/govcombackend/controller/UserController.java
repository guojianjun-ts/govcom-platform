package com.gjj.govcombackend.controller;

import com.gjj.govcombackend.annotion.UserAuthCheck;
import com.gjj.govcombackend.common.BaseResponse;
import com.gjj.govcombackend.common.ResultUtils;
import com.gjj.govcombackend.exception.ErrorCode;
import com.gjj.govcombackend.exception.ThrowUtils;
import com.gjj.govcombackend.model.dto.user.UserLoginRequest;
import com.gjj.govcombackend.model.dto.user.UserRegisterRequest;
import com.gjj.govcombackend.model.dto.user.UserUpdatePasswordRequest;
import com.gjj.govcombackend.model.dto.user.UserUpdateRequest;
import com.gjj.govcombackend.model.entity.User;
import com.gjj.govcombackend.model.vo.LoginUserVO;
import com.gjj.govcombackend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     */
    @ApiOperation(value = "用户注册", notes = "新用户注册接口")
    @ApiImplicitParam(name = "userRegisterRequest", value = "用户注册请求参数", required = true, dataType = "UserRegisterRequest")
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     */
    @ApiOperation(value = "用户登录", notes = "用户登录接口")
    @ApiImplicitParam(name = "userLoginRequest", value = "用户登录请求参数", required = true, dataType = "UserLoginRequest")
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 修改当前登录用户信息
     */
    @ApiOperation(value = "修改用户信息", notes = "修改当前登录用户的信息")
    @UserAuthCheck
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,
                                            HttpServletRequest request) {
        ThrowUtils.throwIf(userUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        boolean result = userService.updateCurrentUser(userUpdateRequest, request);
        return ResultUtils.success(result);
    }

    /**
     * 修改密码
     */
    @ApiOperation(value = "修改密码", notes = "修改当前登录用户的密码")
    @UserAuthCheck
    @PostMapping("/update/password")
    public BaseResponse<Boolean> updatePassword(@RequestBody UserUpdatePasswordRequest passwordRequest,
                                                HttpServletRequest request) {
        ThrowUtils.throwIf(passwordRequest == null, ErrorCode.PARAMS_ERROR);
        boolean result = userService.updatePassword(
                passwordRequest.getOldPassword(),
                passwordRequest.getNewPassword(),
                passwordRequest.getCheckPassword(),
                request
        );
        return ResultUtils.success(result);
    }


    /**
     * 用户登出
     */
    @ApiOperation(value = "用户登出", notes = "用户登出接口")
    @UserAuthCheck
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }


    /**
     * 获取当前登录用户
     */
    @ApiOperation(value = "获取登录用户信息", notes = "获取当前登录用户的详细信息")
    @UserAuthCheck
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(user));
    }
}