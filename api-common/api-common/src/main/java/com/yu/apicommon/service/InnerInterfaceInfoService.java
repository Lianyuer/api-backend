package com.yu.apicommon.service;

import com.yu.apicommon.model.entity.InterfaceInfo;

/**
 * @author liany
 */
public interface InnerInterfaceInfoService {

    /**
     * 数据库中查询模拟接口是否存在（请求路径、请求方法、请求参数）
     *
     * @param path
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String path, String method);

}
