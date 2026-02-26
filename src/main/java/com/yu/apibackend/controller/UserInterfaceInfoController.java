package com.yu.apibackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yu.apibackend.annotation.AuthCheck;
import com.yu.apibackend.common.BaseResponse;
import com.yu.apibackend.common.DeleteRequest;
import com.yu.apibackend.common.ErrorCode;
import com.yu.apibackend.common.ResultUtils;
import com.yu.apibackend.constant.UserConstant;
import com.yu.apibackend.exception.BusinessException;
import com.yu.apibackend.exception.ThrowUtils;
import com.yu.apibackend.model.dto.userinterfaceinfo.UserInterfaceInfoAddRequest;
import com.yu.apibackend.model.dto.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import com.yu.apibackend.model.dto.userinterfaceinfo.UserInterfaceInfoUpdateRequest;
import com.yu.apicommon.model.entity.InterfaceInfo;
import com.yu.apicommon.model.entity.User;
import com.yu.apibackend.service.InterfaceInfoService;
import com.yu.apibackend.service.UserInterfaceInfoService;
import com.yu.apibackend.service.UserService;
import com.yu.apicommon.model.entity.UserInterfaceInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/userInterfaceInfo")
public class UserInterfaceInfoController {

    @Resource
    private UserService userService;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    /**
     * 创建
     *
     * @param userInterfaceInfoAddRequest
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUserInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest userInterfaceInfoAddRequest) {
        if (userInterfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoAddRequest, userInterfaceInfo);
        // 校验
        userInterfaceInfoService.validateUserInterfaceInfo(userInterfaceInfo, true);
        // 判断用户是否存在
        Long userId = userInterfaceInfoAddRequest.getUserId();
        User user = userService.getById(userId);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        // 判断接口是否存在
        Long interfaceInfoId = userInterfaceInfoAddRequest.getInterfaceInfoId();
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(interfaceInfoId);
        ThrowUtils.throwIf(interfaceInfo == null, ErrorCode.NOT_FOUND_ERROR, "接口不存在");
        // 判断用户调用接口记录是否存在
        boolean isUserInterfaceInfoExist = userInterfaceInfoService.lambdaQuery()
                .eq(UserInterfaceInfo::getUserId, userId)
                .eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfoId)
                .exists();
        ThrowUtils.throwIf(isUserInterfaceInfoExist, ErrorCode.PARAMS_ERROR, "用户接口调用关系已存在");
        boolean isSaved = userInterfaceInfoService.save(userInterfaceInfo);
        ThrowUtils.throwIf(!isSaved, ErrorCode.OPERATION_ERROR);
        Long newUserInterfaceInfoId = userInterfaceInfo.getId();
        return ResultUtils.success(newUserInterfaceInfoId);
    }

    /**
     * 更新
     *
     * @param userInterfaceInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> updateUserInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest,
                                                      HttpServletRequest request) {
        if (userInterfaceInfoUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoUpdateRequest, userInterfaceInfo);
        User loginUser = userService.getLoginUser(request);
        Long id = userInterfaceInfoUpdateRequest.getId();
        // 校验
        userInterfaceInfoService.validateUserInterfaceInfo(userInterfaceInfo, false);
        // 判断是否存在
        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldUserInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人和管理员可以修改
        if (!oldUserInterfaceInfo.getUserId().equals(loginUser.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean isUpdated = userInterfaceInfoService.updateById(userInterfaceInfo);
        ThrowUtils.throwIf(!isUpdated, ErrorCode.OPERATION_ERROR);
        Long newUserInterfaceInfoId = userInterfaceInfo.getId();
        return ResultUtils.success(newUserInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUserInterfaceInfo(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() <= 0, ErrorCode.PARAMS_ERROR);
        boolean isSuccess = userInterfaceInfoService.removeById(deleteRequest.getId());
        ThrowUtils.throwIf(!isSuccess, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 查询
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<UserInterfaceInfo> getUserInterfaceInfoById(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getById(id);
        return ResultUtils.success(userInterfaceInfo);
    }

    /**
     * 查询
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @GetMapping("/list")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<UserInterfaceInfo>> listUserInterfaceInfo(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        QueryWrapper<UserInterfaceInfo> queryWrapper = userInterfaceInfoService.getQueryWrapper(userInterfaceInfoQueryRequest);
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoService.list(queryWrapper);
        return ResultUtils.success(userInterfaceInfoList);
    }

    /**
     * 分页查询
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @GetMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserInterfaceInfo>> listUserInterfaceInfoByPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        int current = userInterfaceInfoQueryRequest.getCurrent();
        int pageSize = userInterfaceInfoQueryRequest.getPageSize();
        QueryWrapper<UserInterfaceInfo> queryWrapper = userInterfaceInfoService.getQueryWrapper(userInterfaceInfoQueryRequest);
        Page<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoService.page(new Page<>(current, pageSize), queryWrapper);
        return ResultUtils.success(userInterfaceInfoList);
    }
}
