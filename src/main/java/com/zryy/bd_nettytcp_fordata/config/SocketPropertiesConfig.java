package com.zryy.bd_nettytcp_fordata.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 配置类
 *
 * @author Lizb
 * @version 1.0
 * @date 2023/3/22 16:49:22
 */
@Data
@Setter
@Getter
@ToString
@Component
@Configuration
public class SocketPropertiesConfig {

    @Value("${socket.host}")
    private String host;

    @Value("${socket.port}")
    private Integer port;


}
