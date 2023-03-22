package com.zryy.bd_nettytcp_fordata.config;

import cn.hutool.cron.CronUtil;
import com.zryy.bd_nettytcp_fordata.server.NettyServer;
import com.zryy.bd_nettytcp_fordata.service.HexToAllFormatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;

/**
 * @author Lizb
 * @version 1.0
 * @date 2023/3/22 16:52:00
 */
@Slf4j
@Component
public class LaunchRunner implements CommandLineRunner {

    private NettyServer nettyServer;

    @Autowired
    public void setNettyServer(NettyServer nettyServer) {
        this.nettyServer = nettyServer;
    }

    private SocketPropertiesConfig socketProperties;

    @Autowired
    public void setSocketProperties(SocketPropertiesConfig socketProperties) {
        this.socketProperties = socketProperties;
    }


    @Override
    public void run(String... args) throws Exception {
        TaskRunner();
        InetSocketAddress address = new InetSocketAddress(socketProperties.getHost(), socketProperties.getPort());
        log.info("netty服务器启动地址:" + socketProperties.getHost());
        nettyServer.start(address);
    }

    /**
     * 执行正在运行的任务
     */
    private void TaskRunner() {
        /**
         * 任务队列启动
         */
        CronUtil.setMatchSecond(true);
        CronUtil.start();
        log.info("\n-----------------------任务服务启动------------------------\n\t" +
                        "当前正在启动的{}个任务" +
                        "\n-----------------------------------------------------------\n\t"
                , CronUtil.getScheduler().size()

        );
    }


}
