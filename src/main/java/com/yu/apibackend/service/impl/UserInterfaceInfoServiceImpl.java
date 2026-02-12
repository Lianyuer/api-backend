package com.yu.apibackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yu.apibackend.common.ErrorCode;
import com.yu.apibackend.constant.CommonConstant;
import com.yu.apibackend.exception.ThrowUtils;
import com.yu.apibackend.mapper.UserInterfaceInfoMapper;
import com.yu.apibackend.model.dto.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import com.yu.apibackend.model.entity.UserInterfaceInfo;
import com.yu.apibackend.model.enums.UserInterfaceInfoStatusEnum;
import com.yu.apibackend.service.UserInterfaceInfoService;
import com.yu.apibackend.utils.SqlUtils;
import org.springframework.stereotype.Service;

/**
 * @author liany
 * @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service实现
 * @createDate 2026-02-07 11:19:42
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService {

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
}
