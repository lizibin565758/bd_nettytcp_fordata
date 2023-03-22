package com.zryy.bd_nettytcp_fordata.config;

import com.zryy.bd_nettytcp_fordata.handler.NettyServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author Lizb
 * @version 1.0
 * @date 2023/3/22 16:59:07
 */
public class NettyServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //接收消息格式,使用自定义解析数据格式
        pipeline.addLast("decoder", new Decoder());
        //发送消息格式，使用自定义解析数据格式
        pipeline.addLast("encoder", new Encoder());

        //针对客户端，如果在1分钟时没有想服务端发送写心跳(ALL)，则主动断开
        //如果是读空闲或者写空闲，不处理,这里根据自己业务考虑使用
        //pipeline.addLast(new IdleStateHandler(600,0,0, TimeUnit.SECONDS));
        //自定义的空闲检测
        pipeline.addLast(new NettyServerHandler());
    }
}
