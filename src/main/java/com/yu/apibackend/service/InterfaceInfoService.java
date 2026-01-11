package com.yu.apibackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yu.apibackend.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.yu.apibackend.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yu.apibackend.model.vo.InterfaceInfoVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liany
 * @description 针对表【interface_info(接口信息表)】的数据库操作Service
 * @createDate 2026-01-11 16:15:31
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 校验
     *
     * @param interfaceInfo
     * @param add
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

    /**
     * 获取接口信息包装信息
     *
     * @param interfaceInfo
     * @param request
     * @return
     */
    InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo, HttpServletRequest request);

    /**
     * 获取查询包装类
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

    /**
     * 获取接口包装信息分页列表
     *
     * @param interfaceInfoPage
     * @param request
     * @return
     */
    Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request);
}
