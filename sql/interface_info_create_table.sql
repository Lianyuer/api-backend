-- 创建数据库
create database if not exists api_open_platform;

use api_open_platform;

-- 创建接口信息表
create table interface_info
(
    id             bigint                             not null comment '主键id'
        primary key,
    name           varchar(256)                       not null comment '接口名称',
    description    varchar(256)                       null comment '接口描述',
    url            varchar(512)                       not null comment '接口地址',
    requestParams  text                               null comment '请求参数',
    requestHeader  varchar(512)                       null comment '请求头',
    responseHeader varchar(512)                       null comment '响应头',
    status         tinyint  default 0                 not null comment '接口状态 0-关闭，1-开启',
    method         varchar(256)                       not null comment '请求类型',
    userId         bigint                             not null comment '创建人id',
    createTime     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete       tinyint  default 0                 null comment '是否删除，默认否（0）'
)
    comment '接口信息表';

# 创建用户调用接口关系表
create table user_interface_info
(
    id              bigint                             not null comment '主键id'
        primary key,
    userId          bigint                             not null comment '调用用户id',
    interfaceInfoId bigint                             not null comment '接口id',
    totalNum        int      default 0                 not null comment '总调用次数',
    leftNum         int      default 0                 not null comment '剩余可调用次数',
    status          int      default 0                 not null comment '状态（0-正常，1-禁用）',
    isDeleted       tinyint  default 0                 not null comment '是否删除（0-未删，1-已删）',
    createTime      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '用户调用接口关系表';
