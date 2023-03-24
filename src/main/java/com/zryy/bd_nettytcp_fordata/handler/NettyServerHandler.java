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
import java.util.Arrays;

import static com.zryy.bd_nettytcp_fordata.constant.CodeConstant.FunctionCode.*;
import static com.zryy.bd_nettytcp_fordata.utils.CrossoverToolUtils.str2HexStr;
import static com.zryy.bd_nettytcp_fordata.utils.CrossoverToolUtils.strDecToHex;

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

    // 构造器方式注入
    private HexToAllFormatService hexToAllFormatService;

    @Autowired
    public void setHexToAllFormatService(HexToAllFormatService hexToAllFormatService) {
        this.hexToAllFormatService = hexToAllFormatService;
    }

    /**
     * 有客户端连接服务器会触发此函数
     *
     * @author Lizb
     * @date 2023/3/23 09:30:15
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress inSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = inSocket.getAddress().getHostAddress();
        int clientPort = inSocket.getPort();
        // 获取连接通道唯一标识
        ChannelId channelId = ctx.channel().id();
        // 如果map中不包含此连接，就保存连接
        if (ChannelMap.getChannelMap().containsKey(channelId)) {
            log.info(" 🚀 客户端:{}, 是连接状态，连接通道数量:{} ", channelId, ChannelMap.getChannelMap().size());
        } else {
            // 保存连接
            ChannelMap.addChannel(channelId, ctx.channel());
            log.info(" 🚀 客户端:{}, 连接netty服务器[ IP:{} ===>>> PORT:{} ]", channelId, clientIp, clientPort);
            log.info(" 🚀 连接通道数量: {}", ChannelMap.getChannelMap().size());
        }
    }

    /**
     * 有客户端(设备采集器/上位机)发消息会触发此函数
     *
     * @author Lizb
     * @date 2023/3/23 09:30:05
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info(" 🚀 收到客户端报文, 客户端id:{}, 客户端消息:{}", ctx.channel().id(), msg);

        // 回复客户端(设备采集器/上位机)消息
        this.channelWrite(ctx, msg);
        // 进入参数逻辑
        nettyServerHandler.hexToAllFormatService.hexToCutOut(ctx, msg);

    }

    /**
     * 服务端 给 客户端(设备采集器/上位机) 返回/发送消息
     *
     * @author Lizb
     * @date 2023/3/23 09:41:11
     */
    public void channelWrite(ChannelHandlerContext ctx, Object msg) throws Exception {
        ChannelId channelId = ctx.channel().id();
        Channel channel = ChannelMap.getChannelMap().get(channelId);

        // 判断功能码
        String functionCode = String.valueOf(msg).substring(10, 12);

        if (ctx.channel().id() == null) {
            log.warn(" 🚀 通道:{}, 不存在", ctx.channel().id());
            return;
        }
        if (msg == null || msg == "") {
            log.warn(" 🚀 服务端响应空的消息");
            return;
        }

        // 上位机回复包, 接到心跳或设备心跳包 必须回复; 否则会造成设备异常断开
        String msgBytes = "3028AA0879750D0A";
        // 将客户端的信息直接返回写入ctx
        channel.writeAndFlush(msgBytes);
        // 刷新缓存区
        channel.flush();
    }

    /**
     * 有客户端终止连接服务器会触发此函数
     *
     * @author Lizb
     * @date 2023/3/23 09:30:41
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
            log.info(" 🚀 客户端:{}, 连接Netty服务器[ IP:{} ===>>> PORT:{} ]", channelId, clientIp, inSocket.getPort());
            log.info(" 🚀 连接通道数量: " + ChannelMap.getChannelMap().size());
        }
    }

    /**
     * 处理异常
     *
     * @author Lizb
     * @date 2023/3/23 09:40:27
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        log.error(" 🚀 异常 ==>> {}:发生了错误,此连接被关闭! 此时连通数量:{}", ctx.channel().id(), ChannelMap.getChannelMap().size());
    }

}
