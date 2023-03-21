package com.zryy.bd_nettytcp_fordata.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 编写Decoder, 基于16进制传输数据报文, 防止乱码
 *
 * @author Lizb
 * @version 1.0
 * @date 2023/3/20 09:31:34
 */
@Component
public class Decoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        //创建字节数组,buffer.readableBytes可读字节长度
        byte[] b = new byte[buffer.readableBytes()];
        //复制内容到字节数组b
        buffer.readBytes(b);
        //字节数组转字符串
        String str = new String(b);
        out.add(toHexString1(b));
    }


    public static String toHexString1(byte[] b) {
        StringBuilder buffer = new StringBuilder();
        for (byte value : b) {
            buffer.append(toHexString1(value));
        }
        return buffer.toString();
    }

    public static String toHexString1(byte b) {
        String s = Integer.toHexString(b & 0xFF);
        if (s.length() == 1) {
            return "0" + s.toUpperCase();
        } else {
            return s.toUpperCase();
        }
    }

}
