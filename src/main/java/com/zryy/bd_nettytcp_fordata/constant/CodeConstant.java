package com.zryy.bd_nettytcp_fordata.constant;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局_变量/常量
 *
 * @author Lizb
 * @version 1.0
 * @date 2023/3/17 11:19:31
 */
@Data
@Component
public class CodeConstant {

    /**
     * channelId和设备Id的对应关系
     *
     * @author Lizb
     * @date 2023/3/24 16:14:28
     */
    public static Map<ChannelId, String>  correlationId = new HashMap<>();

    public interface FunctionCode {

        /**
         * 加油时间公式固定所用常量
         */
        int BASE_VALUE = 20;

        /**
         * 判断加油数据大小所用
         */
        int CODELENGTH = 138;

        /*
        {重启指令
        配置设备时钟
        配置IP地址及端口号} 的通用起始标志
        */
        String STARTFLAG = "5AAAAAAAA";
        String IPCONDITIONCODE = "49503D";


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

        /**
         * 结束码
         */
        String ENDCODE = "0D0A";
    }


}
