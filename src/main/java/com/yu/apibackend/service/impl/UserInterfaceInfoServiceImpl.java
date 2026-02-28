package com.yu.apibackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yu.apibackend.common.ErrorCode;
import com.yu.apibackend.constant.CommonConstant;
import com.yu.apibackend.exception.BusinessException;
import com.yu.apibackend.exception.ThrowUtils;
import com.yu.apibackend.mapper.UserInterfaceInfoMapper;
import com.yu.apibackend.model.dto.userinterfaceinfo.UserInterfaceInfoAddRequest;
import com.yu.apibackend.model.dto.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import com.yu.apibackend.model.enums.UserInterfaceInfoStatusEnum;
import com.yu.apibackend.service.InterfaceInfoService;
import com.yu.apibackend.service.UserInterfaceInfoService;
import com.yu.apibackend.service.UserService;
import com.yu.apibackend.utils.SqlUtils;
import com.yu.apicommon.model.entity.InterfaceInfo;
import com.yu.apicommon.model.entity.User;
import com.yu.apicommon.model.entity.UserInterfaceInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author liany
 * @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service实现
 * @createDate 2026-02-07 11:19:42
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService {

    @Resource
    private UserService userService;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    /**
     * 校验
     *
     * @param userInterfaceInfo
     * @param isAdd
     */
    @Override
    public void validateUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean isAdd) {
        ThrowUtils.throwIf(userInterfaceInfo == null, ErrorCode.PARAMS_ERROR);
        Long id = userInterfaceInfo.getId();
        Long userId = userInterfaceInfo.getUserId();
        Long interfaceInfoId = userInterfaceInfo.getInterfaceInfoId();
        Integer totalNum = userInterfaceInfo.getTotalNum();
        Integer leftNum = userInterfaceInfo.getLeftNum();
        Integer status = userInterfaceInfo.getStatus();
        if (isAdd) {
            ThrowUtils.throwIf(userId <= 0 || interfaceInfoId <= 0, ErrorCode.PARAMS_ERROR, "接口或用户不存在");
        } else {
            ThrowUtils.throwIf(id == null, ErrorCode.PARAMS_ERROR, "id缺失");
            ThrowUtils.throwIf(UserInterfaceInfoStatusEnum.getEnumByValue(status) == null, ErrorCode.PARAMS_ERROR, "状态不存在");
        }
        ThrowUtils.throwIf(totalNum < 0, ErrorCode.PARAMS_ERROR, "总调用次数不能小于0");
        ThrowUtils.throwIf(leftNum < 0, ErrorCode.PARAMS_ERROR, "剩余可调用次数不能小于0");
    }

    /**
     * 获取查询包装对象
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        Long id = userInterfaceInfoQueryRequest.getId();
        Long userId = userInterfaceInfoQueryRequest.getUserId();
        Long interfaceInfoId = userInterfaceInfoQueryRequest.getInterfaceInfoId();
        Integer totalNum = userInterfaceInfoQueryRequest.getTotalNum();
        Integer leftNum = userInterfaceInfoQueryRequest.getLeftNum();
        Integer status = userInterfaceInfoQueryRequest.getStatus();
        String sortField = userInterfaceInfoQueryRequest.getSortField();
        String sortOrder = userInterfaceInfoQueryRequest.getSortOrder();

        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(userId != null, "userId", userId);
        queryWrapper.eq(interfaceInfoId != null, "interfaceInfoId", interfaceInfoId);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 创建用户调用接口关系
     *
     * @param userInterfaceInfoAddRequest
     * @return
     */
    @Override
    public Long addUserInterfaceInfo(UserInterfaceInfoAddRequest userInterfaceInfoAddRequest) {
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoAddRequest, userInterfaceInfo);
        // 校验
        this.validateUserInterfaceInfo(userInterfaceInfo, true);
        // 判断用户是否存在
        Long userId = userInterfaceInfoAddRequest.getUserId();
        User user = userService.getById(userId);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        // 判断接口是否存在
        Long interfaceInfoId = userInterfaceInfoAddRequest.getInterfaceInfoId();
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(interfaceInfoId);
        ThrowUtils.throwIf(interfaceInfo == null, ErrorCode.NOT_FOUND_ERROR, "接口不存在");
        // 判断用户调用接口记录是否存在
        boolean isUserInterfaceInfoExist = this.lambdaQuery()
                .eq(UserInterfaceInfo::getUserId, userId)
                .eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfoId)
                .exists();
        ThrowUtils.throwIf(isUserInterfaceInfoExist, ErrorCode.PARAMS_ERROR, "用户接口调用关系已存在");
        boolean isSaved = this.save(userInterfaceInfo);
        ThrowUtils.throwIf(!isSaved, ErrorCode.OPERATION_ERROR);
        return userInterfaceInfo.getId();
    }

    /**
     * 调用接口统计
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        // 判断
        ThrowUtils.throwIf(interfaceInfoId <= 0 || userId <= 0, ErrorCode.PARAMS_ERROR);
        // 使用 UpdateWrapper 对象来构建更新条件
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        // 在 updateWrapper 中设置了两个条件：interfaceInfoId 等于给定的 interfaceInfoId 和 userId 等于给定的 userId。
        updateWrapper.eq("interfaceInfoId", interfaceInfoId)
                .eq("userId", userId)
                .gt("leftNum", 0)  // 确保剩余次数大于0
                .setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
        // 最后，调用update方法执行更新操作，并返回更新是否成功的结果
        int rows = userInterfaceInfoMapper.update(null, updateWrapper);
        if (rows > 0) {
            // 扣减成功
            return true;
        } else {
            UserInterfaceInfo userInterfaceInfo = this.lambdaQuery()
                    .eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfoId)
                    .eq(UserInterfaceInfo::getUserId, userId).one();
            Integer leftNum = userInterfaceInfo == null ? null : userInterfaceInfo.getLeftNum();
            if (leftNum == null) {
                // 记录不存在，需要创建（首次调用）
                UserInterfaceInfoAddRequest userInterfaceInfoAddRequest = new UserInterfaceInfoAddRequest();
                userInterfaceInfoAddRequest.setInterfaceInfoId(interfaceInfoId);
                userInterfaceInfoAddRequest.setUserId(userId);
                userInterfaceInfoAddRequest.setTotalNum(1);
                userInterfaceInfoAddRequest.setLeftNum(999);
                addUserInterfaceInfo(userInterfaceInfoAddRequest);
            } else if (leftNum <= 0) {
                // 次数已用完
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "调用次数不足");
            }
            return false;
        }
    }
}
