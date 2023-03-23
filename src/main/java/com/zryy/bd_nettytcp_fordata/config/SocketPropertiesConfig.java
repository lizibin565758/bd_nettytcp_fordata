package com.zryy.bd_nettytcp_fordata.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类
 *
 * @author Lizb
 * @version 1.0
 * @date 2023/3/22 16:49:22
 */
@Data
@Configuration
public class SocketPropertiesConfig {

    @Value("${socket.host}")
    private String host;

    @Value("${socket.port}")
    private Integer port;


}
