package com.zryy.bd_nettytcp_fordata.manage;

import com.zryy.bd_nettytcp_fordata.config.ChannelMap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelId;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.zryy.bd_nettytcp_fordata.constant.CodeConstant.FunctionCode.*;
import static com.zryy.bd_nettytcp_fordata.utils.CrossoverToolUtils.*;

/**
 * 功能描述: 定时发送TCP报文
 * TODO 按需求更改即可
 *
 * @author Lizb
 * @version 1.0
 * @date 2023/3/23 09:22:53
 */
@Slf4j
@Component
public class TcpManage {

    public static void main(String[] args) {
        sendMsg();
    }

    public static void sendMsg() {
        ConcurrentHashMap<ChannelId, Channel> channelMap = ChannelMap.getChannelMap();
        if (CollectionUtils.isEmpty(channelMap)) {
            return;
        }
        ConcurrentHashMap.KeySetView<ChannelId, Channel> channelIds = channelMap.keySet();

        /* 配置IP */
        // 写出要修改的数据区, 例如修改IP和端口 格式为: 42.105.60.1 14,6001/
        String ipStr = "27.105.60.1 14,6001/";
        // 调用ASCII转换十六进制工具
        String ipHex = str2HexStr(ipStr);
        String ipOrProtHex = ipHex.replace(" ", "");
        // 配置IP地址及端口号 HEX
        String msgStr = STARTFLAG + IPCONDITIONCODE + ipOrProtHex + ENDCODE;
        System.out.println(msgStr);

        /* 配置时钟 */
        String year = strDecToHex(2020);
        String mon = strDecToHex(11);
        String day = strDecToHex(03);
        String h = strDecToHex(18);
        String m = strDecToHex(30);
        String s = strDecToHex(00);

        String timeMsg = year + mon + day + h + m + s;
        System.out.println(timeMsg);

        System.out.println(msgStr);
        for (ChannelId channelId : channelIds) {
            Channel channel = ChannelMap.getChannelByName(channelId);
            // 判断是否活跃
            if (channel == null || !channel.isActive()) {
                ChannelMap.getChannelMap().remove(channelId);
                log.info("客户端:{},连接已经中断", channelId);
                return;
            }
            // 指令发送
            ByteBuf buffer = Unpooled.buffer();
            log.info("开始发送报文:{}", Arrays.toString(msgStr.toCharArray()));
            buffer.writeBytes(msgStr.getBytes());
            channel.writeAndFlush(buffer).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("客户端:{},回写成功:{}", channelId, Arrays.toString(msgStr.toCharArray()));
                } else {
                    log.warn("客户端:{},回写失败:{}", channelId, Arrays.toString(msgStr.toCharArray()));
                }
            });
        }
    }

    /**
     * 功能描述: 定时删除不活跃的连接
     *
     * @return void
     * @Author keLe
     * @Date 2022/8/26
     */
    public void deleteInactiveConnections() {
        ConcurrentHashMap<ChannelId, Channel> channelMap = ChannelMap.getChannelMap();
        if (!CollectionUtils.isEmpty(channelMap)) {
            for (Map.Entry<ChannelId, Channel> next : channelMap.entrySet()) {
                ChannelId channelId = next.getKey();
                Channel channel = next.getValue();
                if (!channel.isActive()) {
                    channelMap.remove(channelId);
                    log.info("客户端:{},连接已经中断", channelId);
                }
            }
        }
    }

}
