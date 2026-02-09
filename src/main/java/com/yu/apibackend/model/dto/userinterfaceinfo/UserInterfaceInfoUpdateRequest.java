package com.yu.apibackend.model.dto.userinterfaceinfo;

import lombok.Data;

@Data
public class UserInterfaceInfoUpdateRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 接口 id
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

    /**
     * 状态（0-正常，1-禁用）
     */
    private Integer status;

    /**
     * 是否删除（0-未删，1-已删）
     */
    private Integer isDeleted;

}
