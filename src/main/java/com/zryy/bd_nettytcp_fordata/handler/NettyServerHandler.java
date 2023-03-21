package com.zryy.bd_nettytcp_fordata.handler;

import com.zryy.bd_nettytcp_fordata.service.HexToAllFormatService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Netty业务处理handler
 *
 * @author Lizb
 * @version 1.0
 * @date 2023/3/17 09:34:59
 */
@Slf4j
@Component
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {

    public static NettyServerHandler nettyServerHandler;

    public @PostConstruct void init() {
        nettyServerHandler = this;
    }

    // 构造器方式注入, @RequiredArgsConstructor的方式在handler中不可用
    private HexToAllFormatService hexToAllFormatService;

    @Autowired
    public void setHexToAllFormatService(HexToAllFormatService hexToAllFormatService) {
        this.hexToAllFormatService = hexToAllFormatService;
    }


    // 定义一个channle 组，管理所有的channel
    // GlobalEventExecutor.INSTANCE) 是全局的事件执行器，是一个单例
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 有客户端与服务器发生连接时执行此方法
     * 1.打印提示信息
     * 2.将客户端保存到 channelGroup 中
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        log.info(" 🚀 有新的客户端与服务器发生连接, 客户端地址：" + channel.remoteAddress());
        channelGroup.add(channel);
    }

    /**
     * 表示channel 处于活动状态
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info(" 🚀 " + ctx.channel().remoteAddress() + " 处于活动状态");
    }

    /**
     * 读取到客户端发来的数据数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 获取到当前channel
        Channel channel = ctx.channel();
        log.info(" 🚀 有客户端发来的数据。地址：" + channel.remoteAddress() + " 内容：" + msg);
        // 必须回复上位机, 否则会造成设备异常断开;
        ctx.write("30 28 AA 08 79 75 0D 0A");
        ctx.flush();
        // 进入参数逻辑
        nettyServerHandler.hexToAllFormatService.hexToCutOut(msg);

    }

    /**
     * 当有客户端与服务器断开连接时执行此方法，此时会自动将此客户端从 channelGroup 中移除
     * 1.打印提示信息
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        log.error(" 🚀 有客户端与服务器断开连接。客户端地址：" + channel.remoteAddress());
    }

    /**
     * 表示channel 处于不活动状态
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.error(" 🚀 " + ctx.channel().remoteAddress() + " 处于不活动状态");
    }

    /**
     * 处理异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(" 🚀 发生异常。异常信息：{}", cause.getMessage());
        cause.printStackTrace();
        // 关闭通道
        ctx.close();
    }

}
