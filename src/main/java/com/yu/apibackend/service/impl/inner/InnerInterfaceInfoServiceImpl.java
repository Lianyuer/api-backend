package com.yu.apibackend.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yu.apibackend.common.ErrorCode;
import com.yu.apibackend.exception.ThrowUtils;
import com.yu.apibackend.service.InterfaceInfoService;
import com.yu.apicommon.model.entity.InterfaceInfo;
import com.yu.apicommon.service.InnerInterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 数据库中查询模拟接口是否存在（请求路径、请求方法、请求参数）
     *
     * @param path
     * @param method
     * @return
     */
    @Override
    public InterfaceInfo getInterfaceInfo(String path, String method) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(path) || StringUtils.isAnyBlank(method), ErrorCode.PARAMS_ERROR);
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", path);
        queryWrapper.eq("method", method);
        return interfaceInfoService.getOne(queryWrapper);
    }
}
