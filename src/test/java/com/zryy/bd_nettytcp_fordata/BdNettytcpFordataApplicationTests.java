package com.zryy.bd_nettytcp_fordata;

import com.zryy.bd_nettytcp_fordata.manage.TcpManage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class BdNettytcpFordataApplicationTests {

    @Autowired
    TcpManage tcpManage;

    @Test
    void sendMsgData() {
        tcpManage.sendMsg();
    }

}
