package com.yu.apibackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yu.apibackend.model.dto.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import com.yu.apibackend.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author liany
 * @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service
 * @createDate 2026-02-07 11:19:42
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 校验
     *
     * @param userInterfaceInfo
     * @param isAdd
     */
    void validateUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean isAdd);

    /**
     * 获取查询包装对象
     *
     * @return
     */
    QueryWrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest);

}
