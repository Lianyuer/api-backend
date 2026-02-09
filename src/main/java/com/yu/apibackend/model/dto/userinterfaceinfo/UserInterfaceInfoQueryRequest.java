package com.yu.apibackend.model.dto.userinterfaceinfo;

import lombok.Data;

@Data
public class UserInterfaceInfoQueryRequest {

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 接口 id
     */
    private Long interfaceInfoId;
}
