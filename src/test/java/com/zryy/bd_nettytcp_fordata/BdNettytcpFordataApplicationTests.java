package com.zryy.bd_nettytcp_fordata;

import com.zryy.bd_nettytcp_fordata.utils.CrossoverToolUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class BdNettytcpFordataApplicationTests {


    @Test
    void hexToAsciiTest() {
        String str = "30 30 30 30 30 30 31 35 32 38";
        CrossoverToolUtils.hexToAscii(str);
    }

}
