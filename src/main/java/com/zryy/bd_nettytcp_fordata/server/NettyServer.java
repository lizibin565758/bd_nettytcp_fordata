package com.zryy.bd_nettytcp_fordata.server;

import com.zryy.bd_nettytcp_fordata.config.Decoder;
import com.zryy.bd_nettytcp_fordata.config.Encoder;
import com.zryy.bd_nettytcp_fordata.config.NettyServerChannelInitializer;
import com.zryy.bd_nettytcp_fordata.handler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * netty服务器，主要用于与客户端通讯
 *
 * @author Lizb
 * @version 1.0
 * @date 2023/3/17 09:11:35
 */
@Slf4j
@Component
public class NettyServer {

    // 编写start方法，处理客户端的请求
    public void start(InetSocketAddress address) {

        // boss 线程组用于处理连接工作 主线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        // work 线程组用于数据处理 工作线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 服务器启动项
            // 绑定线程池：handler => bossGroup，childHandler => workerHandler
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    // nioChannel
                    .channel(NioServerSocketChannel.class)
                    //使用指定的端口设置套接字地址
                    .localAddress(address)
                    // 使用自定义处理类 绑定客户端连接时候触发操作
                    .childHandler(new NettyServerChannelInitializer())
                    // 临时存放已完成三次握手的请求的队列的最大长度 TODO 如果大于队列的最大长度，请求会被拒绝
                    //服务端可连接队列数,对应TCP/IP协议listen函数中backlog参数
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 保持长连接 2小时无数据激活心跳机制
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //将小的数据包包装成更大的帧进行传送，提高网络的负载
                    .childOption(ChannelOption.TCP_NODELAY, true);
            // 服务器异步创建绑定定
            ChannelFuture future = bootstrap.bind(address).sync();
            if (future.isSuccess()) {
                log.info("netty服务器开始监听端口：{}", address.getPort());
            }
            /*
            该方法进行阻塞,等待服务端链路关闭之后继续执行。
            这种模式一般都是使用Netty模块主动向服务端发送请求，然后最后结束才使用
            */
            //关闭channel和块，直到它被关闭
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            // 释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

}
