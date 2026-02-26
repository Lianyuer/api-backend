package com.yu.apicommon.model.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 接口信息状态枚举
 */
@Getter
public enum InterfaceInfoStatusEnum {

    /**
     * GET 请求
     * 用于获取资源
     */
    ONLINE(1, "上线"),

    /**
     * POST 请求
     * 用于创建资源
     */
    OFFLINE(0, "下线"),
    ;


    /**
     * HTTP 方法名称
     */
    private final Integer value;

    /**
     * 方法描述
     */
    private final String text;

    /**
     * 构造函数
     *
     * @param value HTTP 方法名称
     * @param text  方法描述
     */
    InterfaceInfoStatusEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * 根据方法名获取枚举
     *
     * @param method 方法名（不区分大小写）
     * @return 对应的枚举，未找到返回 null
     */
    public static InterfaceInfoStatusEnum getEnumByValue(String method) {
        if (method == null) {
            return null;
        }
        for (InterfaceInfoStatusEnum value : values()) {
            if (Objects.equals(value.value, value.getValue())) {
                return value;
            }
        }
        return null;
    }

}