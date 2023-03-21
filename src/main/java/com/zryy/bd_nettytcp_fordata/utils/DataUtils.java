package com.zryy.bd_nettytcp_fordata.utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

/**
 * 时间工具类
 *
 * @author Lizb
 * @version 1.0
 * @date 2023/3/9 15:09:16
 */
@Component
public class DataUtils {
    /**
     * 时间戳毫秒级 转换为年-月-日 时:分:秒:毫秒
     *
     * @author Lizb
     * @date 2023/3/9 15:07:28
     */
    public static String formatTimeYMD_HMS_SSS(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(time);
    }
}
