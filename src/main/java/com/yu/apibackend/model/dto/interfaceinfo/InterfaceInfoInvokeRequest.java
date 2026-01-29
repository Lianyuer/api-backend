package com.yu.apibackend.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    /**
     * 接口 id
     */
    private Long id;

    /**
     * 请求参数
     */
    private String requestParams;

    private static final long serialVersionUID = 1L;
}