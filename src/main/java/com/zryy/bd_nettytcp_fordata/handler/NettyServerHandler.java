package com.zryy.bd_nettytcp_fordata.handler;

import com.zryy.bd_nettytcp_fordata.config.ChannelMap;
import com.zryy.bd_nettytcp_fordata.service.HexToAllFormatService;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;

/**
 * Netty业务处理handler
 *
 * @author Lizb
 * @version 1.0
 * @date 2023/3/17 09:34:59
 */
@Slf4j
@Component
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

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
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        int clientPort = insocket.getPort();
        //获取连接通道唯一标识
        ChannelId channelId = ctx.channel().id();
        //如果map中不包含此连接，就保存连接
        if (ChannelMap.getChannelMap().containsKey(channelId)) {
            log.info("客户端:{},是连接状态，连接通道数量:{} ", channelId, ChannelMap.getChannelMap().size());
        } else {
            //保存连接
            ChannelMap.addChannel(channelId, ctx.channel());
            log.info("客户端:{},连接netty服务器[IP:{}-->PORT:{}]", channelId, clientIp, clientPort);
            log.info("连接通道数量: {}", ChannelMap.getChannelMap().size());
        }
    }

    /**
     * 读取到客户端发来的数据数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
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
        InetSocketAddress inSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = inSocket.getAddress().getHostAddress();
        ChannelId channelId = ctx.channel().id();
        //包含此客户端才去删除
        if (ChannelMap.getChannelMap().containsKey(channelId)) {
            //删除连接
            ChannelMap.getChannelMap().remove(channelId);
            log.info("客户端:{},连接netty服务器[IP:{}-->PORT:{}]", channelId, clientIp, inSocket.getPort());
            log.info("连接通道数量: " + ChannelMap.getChannelMap().size());
        }
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
