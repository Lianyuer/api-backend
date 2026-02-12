package com.yu.apibackend.model.dto.userinterfaceinfo;

import lombok.Data;

@Data
public class UserInterfaceInfoAddRequest {

    /**
     * 调用用户id
     */
    private Long userId;

    /**
     * 接口id
     */
    private Long interfaceInfoId;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余可调用次数
     */
    private Integer leftNum;

}
