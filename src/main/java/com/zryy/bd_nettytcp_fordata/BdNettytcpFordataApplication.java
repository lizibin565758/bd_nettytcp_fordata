package com.zryy.bd_nettytcp_fordata;

import com.zryy.bd_nettytcp_fordata.server.NettyServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BdNettytcpFordataApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BdNettytcpFordataApplication.class, args);
    }

    /**
     * 重写run方法
     *
     * @author Lizb
     * @date 2023/3/17 09:38:40
     */
    @Override
    public void run(String... args) {
        new Thread(() -> {
            try {
                new NettyServer(8000).run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }
}
