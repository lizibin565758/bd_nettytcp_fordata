package com.zryy.bd_nettytcp_fordata.server;

import com.zryy.bd_nettytcp_fordata.handler.Decoder;
import com.zryy.bd_nettytcp_fordata.handler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.util.CharsetUtil;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

/**
 * netty服务器，主要用于与客户端通讯
 *
 * @author Lizb
 * @version 1.0
 * @date 2023/3/17 09:11:35
 */
@Slf4j
public class NettyServer {

    // 监听端口
    private final int port;

    public NettyServer(int port) {
        this.port = port;
    }

    //编写run方法，处理客户端的请求
    public void run() throws Exception {

        // boss 线程组用于处理连接工作 主线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        // work 线程组用于数据处理 工作线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 服务器启动项
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 绑定线程池：handler => bossGroup，childHandler => workerHandler
            serverBootstrap.group(bossGroup, workerGroup)
                    // nioChannel
                    .channel(NioServerSocketChannel.class)
                    // 临时存放已完成三次握手的请求的队列的最大长度 TODO 如果大于队列的最大长度，请求会被拒绝
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 保持长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //将小的数据包包装成更大的帧进行传送，提高网络的负载
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // 绑定客户端连接时候触发操作
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 获取到pipeline
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 向pipeline加入解码器, 设置为自定义的格式
                            pipeline.addLast("decoder", new Decoder());
                            // 向pipeline加入编码器, 为UTF-8格式
                            pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                            // 加入自己的业务处理handler
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            log.info(" 🚀 Netty服务器启动成功port ===>>> {}", port);
            // 服务器异步创建绑定定
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            /*
            该方法进行阻塞,等待服务端链路关闭之后继续执行。
            这种模式一般都是使用Netty模块主动向服务端发送请求，然后最后结束才使用
            */
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

}
