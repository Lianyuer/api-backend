package com.yu.apibackend.model.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 接口信息状态枚举
 */
@Getter
public enum UserInterfaceInfoStatusEnum {


    NORMAL(0, "正常"),

    DISABLED(1, "禁用");

    private final Integer value;

    private final String text;

    UserInterfaceInfoStatusEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * 根据状态值获取枚举
     *
     * @param status 状态值
     * @return 对应的枚举，未找到返回 null
     */
    public static UserInterfaceInfoStatusEnum getEnumByValue(Integer status) {
        if (status == null) {
            return null;
        }
        for (UserInterfaceInfoStatusEnum value : values()) {
            if (Objects.equals(status, value.getValue())) {
                return value;
            }
        }
        return null;
    }

}