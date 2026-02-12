package com.yu.apibackend.model.dto.userinterfaceinfo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.yu.apibackend.common.PageRequest;
import lombok.Data;

@Data
public class UserInterfaceInfoQueryRequest extends PageRequest {

    /**
     * 主键id
     */
    @TableId
    private Long id;

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

    /**
     * 状态（0-正常，1-禁用）
     */
    private Integer status;

}
