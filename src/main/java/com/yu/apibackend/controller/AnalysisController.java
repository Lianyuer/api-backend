package com.yu.apibackend.controller;

import cn.hutool.core.collection.CollectionUtil;
import co.elastic.clients.elasticsearch.tasks.GroupBy;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yu.apibackend.annotation.AuthCheck;
import com.yu.apibackend.common.BaseResponse;
import com.yu.apibackend.common.ErrorCode;
import com.yu.apibackend.common.ResultUtils;
import com.yu.apibackend.constant.UserConstant;
import com.yu.apibackend.exception.ThrowUtils;
import com.yu.apibackend.mapper.UserInterfaceInfoMapper;
import com.yu.apibackend.model.vo.InterfaceInfoVO;
import com.yu.apibackend.service.InterfaceInfoService;
import com.yu.apicommon.model.entity.InterfaceInfo;
import com.yu.apicommon.model.entity.UserInterfaceInfo;
import kotlin.collections.Grouping;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 获取调用次数最多的接口信息列表。
     * 通过用户接口信息表查询调用次数最多的接口ID，再关联查询接口详细信息。
     *
     * @return 接口信息列表，包含调用次数最多的接口信息
     */
    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterfaceInfo() {
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(5);
        Map<Long, List<UserInterfaceInfo>> interfaceInfoIdObjMap = userInterfaceInfoList.stream()
                .collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        // 查询接口信息列表
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", interfaceInfoIdObjMap.keySet());
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        ThrowUtils.throwIf(CollectionUtil.isEmpty(interfaceInfoList), ErrorCode.SYSTEM_ERROR);
        List<InterfaceInfoVO> interfaceInfoVOList = interfaceInfoList.stream()
                .map(interfaceInfo -> {
                    InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
                    BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
                    Integer totalNum = interfaceInfoIdObjMap.get(interfaceInfo.getId()).get(0).getTotalNum();
                    interfaceInfoVO.setTotalNum(totalNum);
                    return interfaceInfoVO;
                }).collect(Collectors.toList());
        return ResultUtils.success(interfaceInfoVOList);
    }

}
