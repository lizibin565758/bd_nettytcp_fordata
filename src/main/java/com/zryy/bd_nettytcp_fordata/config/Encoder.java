package com.zryy.bd_nettytcp_fordata.config;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.springframework.stereotype.Component;

/**
 * 自定义发送消息格式
 * 使用自定义解析数据格式工具类
 *
 * @author Lizb
 * @version 1.0
 * @date 2023/3/22 16:36:46
 */
@Component
public class Encoder extends MessageToByteEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String s, ByteBuf byteBuf) throws Exception {
        //将16进制字符串转为数组
        byteBuf.writeBytes(hexString2Bytes(s));
    }

    /**
     * 功能描述: 16进制字符串转字节数组
     *
     * @param src 16进制字符串
     * @return byte[]
     * @Author keLe
     * @Date 2022/8/26
     */
    public static byte[] hexString2Bytes(String src) {
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = (byte) Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }
}
