package com.zryy.bd_nettytcp_fordata.service;

import io.netty.channel.ChannelHandlerContext;

/**
 * 服务层
 * 处理接收的参数
 *
 * @author Lizb
 * @version 1.0
 * @date 2023/3/20 10:03:35
 */
public interface HexToAllFormatService {


    /**
     * 接收到上位机报文, 并作截取
     *
     * @author Lizb
     * @date 2023/3/20 10:34:10
     */
    void hexToCutOut(ChannelHandlerContext ctx, Object msg);

}
