package com.yu.apibackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yu.apicommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
 * @author liany
 * @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Mapper
 * @createDate 2026-02-07 11:19:42
 * @Entity com.yu.apibackend.model.entity.UserInterfaceInfo
 */
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    // select interfaceInfoId,sum(totalNum) as totalNum from user_interface_info group by interfaceInfoId order by totalNum desc limit n;
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);

}




