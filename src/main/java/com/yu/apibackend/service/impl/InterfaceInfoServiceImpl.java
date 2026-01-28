package com.yu.apibackend.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yu.apibackend.common.ErrorCode;
import com.yu.apibackend.constant.CommonConstant;
import com.yu.apibackend.exception.BusinessException;
import com.yu.apibackend.exception.ThrowUtils;
import com.yu.apibackend.mapper.InterfaceInfoMapper;
import com.yu.apibackend.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.yu.apibackend.model.entity.*;
import com.yu.apibackend.model.enums.InterfaceMethodEnum;
import com.yu.apibackend.model.vo.InterfaceInfoVO;
import com.yu.apibackend.model.vo.UserVO;
import com.yu.apibackend.service.InterfaceInfoService;
import com.yu.apibackend.service.UserService;
import com.yu.apibackend.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author liany
 * @description 针对表【interface_info(接口信息表)】的数据库操作Service实现
 * @createDate 2026-01-11 16:15:31
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Resource
    private UserService userService;

    /**
     * 校验
     *
     * @param interfaceInfo
     * @param add
     */
    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        String requestParams = interfaceInfo.getRequestParams();
        String requestHeader = interfaceInfo.getRequestHeader();
        String responseHeader = interfaceInfo.getResponseHeader();
        String method = interfaceInfo.getMethod();

        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name, url, method), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(name) && name.length() > 25) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
        if (StringUtils.isNotBlank(description) && description.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "描述过长");
        }
        if (StringUtils.isNotBlank(url) && url.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "地址过长");
        }
        if (StringUtils.isNotBlank(requestParams) && requestParams.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数过长");
        }
        if (StringUtils.isNotBlank(requestHeader) && requestHeader.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求头过长");
        }
        if (StringUtils.isNotBlank(responseHeader) && responseHeader.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "响应头过长");
        }
        if (StringUtils.isNotBlank(method) && InterfaceMethodEnum.fromMethod(method) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求类型错误");
        }
    }

    /**
     * 获取接口信息包装信息
     *
     * @param interfaceInfo
     * @param request
     * @return
     */
    @Override
    public InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo, HttpServletRequest request) {
        InterfaceInfoVO interfaceInfoVO = InterfaceInfoVO.objToVo(interfaceInfo);
        // 1. 关联查询用户信息
        Long userId = interfaceInfo.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        interfaceInfoVO.setUserVO(userVO);

        return interfaceInfoVO;
    }

    /**
     * 获取查询包装类
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        if (interfaceInfoQueryRequest == null) {
            return queryWrapper;
        }
        Long id = interfaceInfoQueryRequest.getId();
        String name = interfaceInfoQueryRequest.getName();
        String description = interfaceInfoQueryRequest.getDescription();
        String url = interfaceInfoQueryRequest.getUrl();
        Integer status = interfaceInfoQueryRequest.getStatus();
        String method = interfaceInfoQueryRequest.getMethod();
        Long userId = interfaceInfoQueryRequest.getUserId();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.like(StringUtils.isNotBlank(url), "url", url);
        queryWrapper.eq(ObjectUtil.isNotEmpty(status), "status", status);
        queryWrapper.eq(StringUtils.isNotBlank(method), "method", method);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取接口包装信息分页列表
     *
     * @param interfaceInfoPage
     * @param request
     * @return
     */
    @Override
    public Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request) {
        List<InterfaceInfo> interfaceInfoList = interfaceInfoPage.getRecords();
        Page<InterfaceInfoVO> interfaceInfoVOPage = new Page<>(interfaceInfoPage.getCurrent(), interfaceInfoPage.getSize(), interfaceInfoPage.getTotal());
        if (CollUtil.isEmpty(interfaceInfoList)) {
            return interfaceInfoVOPage;
        }
        // 关联查询用户信息
        Set<Long> userIdSet = interfaceInfoList.stream().map(InterfaceInfo::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        List<InterfaceInfoVO> interfaceInfoVOList = interfaceInfoList.stream().map(interfaceInfo -> {
            InterfaceInfoVO interfaceInfoVO = InterfaceInfoVO.objToVo(interfaceInfo);
            Long userId = interfaceInfo.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            interfaceInfoVO.setUserVO(userService.getUserVO(user));
            return interfaceInfoVO;
        }).toList();
        interfaceInfoVOPage.setRecords(interfaceInfoVOList);
        return interfaceInfoVOPage;
    }
}




