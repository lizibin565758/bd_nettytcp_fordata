package com.zryy.bd_nettytcp_fordata.constant;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 常量
 *
 * @author Lizb
 * @version 1.0
 * @date 2023/3/17 11:19:31
 */
@Component
public class CodeConstant {
    public interface FunctionCode {

        /**
         * 加油时间公式固定所用常量
         */
        int BASE_VALUE = 20;

        /**
         * 判断加油数据大小所用
         */
        int CODELENGTH = 138;

        /**
         * 设备注册包/设备ID (上行)
         */
        String SIGNCODE = "D1";

        /**
         * 设备心跳包 (上行) 1分钟/次
         */
        String HEARTBEATCODE = "33";

        /**
         * 上位机回复包 (下行)
         */
        String UPPERCOMPUTERREPLY = "75";

        /**
         * 重启指令（下⾏）
         */
        String RESTART = "A4";

        /**
         * 配置设备时钟
         */
        String SETTINGCLOCK = "AB";

        /**
         * 配置IP地址及端⼝号
         */
        String CONFIGIPANDPORT = "A8";

        /**
         * 加油数据传输, 功能码
         * 数据格式
         */
        String REFUELINGDATA = "00";

        /**
         * 加油数据_特征码: 01
         */
        String featureCode01 = "01";

        /**
         * 加油数据_特征码: 02
         */
        String featureCode02 = "02";

        /**
         * 加油数据_特征码: 03
         */
        String featureCode03 = "03";

        /**
         * 加油数据_特征码: 04
         */
        String featureCode04 = "04";

        /**
         * 加油数据_特征码: 05
         */
        String featureCode05 = "05";


    }


}
