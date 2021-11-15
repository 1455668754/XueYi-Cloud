package com.xueyi.common.core.constant;

/**
 * 消息队列通用常量
 *
 * @author Ethereal
 */
public class MessageConstant {

    /** 握手次数 */
    public static final Integer CONNECT_TIMES = 4;

    /** 交换机名称 - 数据源 */
    public static final String EXCHANGE_SOURCE = "exchange-source";

    /** 路由key - 数据源 */
    public static final String ROUTING_KEY_SOURCE = "routingKey.datasource";
}