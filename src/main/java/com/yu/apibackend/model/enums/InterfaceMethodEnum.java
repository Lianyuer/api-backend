package com.yu.apibackend.model.enums;

import lombok.Getter;

/**
 * HTTP 请求方法枚举
 * 用于统一管理接口请求类型
 */
@Getter
public enum InterfaceMethodEnum {

    /**
     * GET 请求
     * 用于获取资源
     */
    GET("GET", "获取资源"),

    /**
     * POST 请求
     * 用于创建资源
     */
    POST("POST", "创建资源"),

    /**
     * PUT 请求
     * 用于更新整个资源
     */
    PUT("PUT", "更新资源"),

    /**
     * DELETE 请求
     * 用于删除资源
     */
    DELETE("DELETE", "删除资源"),

    /**
     * PATCH 请求
     * 用于部分更新资源
     */
    PATCH("PATCH", "部分更新"),

    /**
     * HEAD 请求
     * 类似 GET，但只返回响应头
     */
    HEAD("HEAD", "获取头部信息"),

    /**
     * OPTIONS 请求
     * 获取服务器支持的请求方法
     */
    OPTIONS("OPTIONS", "获取支持的方法"),

    /**
     * TRACE 请求
     * 用于诊断，回显请求
     */
    TRACE("TRACE", "诊断请求"),

    /**
     * CONNECT 请求
     * 建立隧道连接
     */
    CONNECT("CONNECT", "建立隧道");

    /**
     * HTTP 方法名称
     */
    private final String method;

    /**
     * 方法描述
     */
    private final String description;

    /**
     * 构造函数
     *
     * @param method      HTTP 方法名称
     * @param description 方法描述
     */
    InterfaceMethodEnum(String method, String description) {
        this.method = method;
        this.description = description;
    }

    /**
     * 根据方法名获取枚举
     *
     * @param method 方法名（不区分大小写）
     * @return 对应的枚举，未找到返回 null
     */
    public static InterfaceMethodEnum fromMethod(String method) {
        if (method == null) {
            return null;
        }
        for (InterfaceMethodEnum value : values()) {
            if (value.method.equalsIgnoreCase(method)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 判断是否为安全方法（只读，不修改资源）
     *
     * @return true 如果方法是安全的
     *//*
    public boolean isSafeMethod() {
        return this == GET || this == HEAD || this == OPTIONS || this == TRACE;
    }

    *//**
     * 判断是否为幂等方法（多次执行结果相同）
     *
     * @return true 如果方法是幂等的
     *//*
    public boolean isIdempotent() {
        return this == GET || this == HEAD || this == PUT || this == DELETE || this == OPTIONS || this == TRACE;
    }

    *//**
     * 判断是否支持请求体
     *
     * @return true 如果方法支持请求体
     *//*
    public boolean supportsRequestBody() {
        return this == POST || this == PUT || this == PATCH;
    }

    *//**
     * 判断是否允许缓存响应
     *
     * @return true 如果响应可以被缓存
     *//*
    public boolean isCacheable() {
        return this == GET || this == HEAD;
    }

    *//**
     * 获取所有安全方法的数组
     *
     * @return 安全方法数组
     *//*
    public static InterfaceMethodEnum[] getSafeMethods() {
        return new InterfaceMethodEnum[]{GET, HEAD, OPTIONS, TRACE};
    }

    *//**
     * 获取所有支持请求体的方法数组
     *
     * @return 支持请求体的方法数组
     *//*
    public static InterfaceMethodEnum[] getMethodsWithRequestBody() {
        return new InterfaceMethodEnum[]{POST, PUT, PATCH};
    }*/
}