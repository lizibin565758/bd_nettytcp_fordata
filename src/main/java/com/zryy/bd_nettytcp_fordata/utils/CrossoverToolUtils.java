package com.zryy.bd_nettytcp_fordata.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import static com.zryy.bd_nettytcp_fordata.constant.CodeConstant.FunctionCode.BASE_VALUE;

/**
 * @author Lizb
 * @version 1.0
 * @date 2023/3/17 13:46:45
 */
@Component
public class CrossoverToolUtils {

    /**
     * 十六进制转换为十进制 (返回int类型)
     *
     * @author Lizb
     * @date 2023/3/21 13:48:14
     */
    public static int hexToDec(String hexData) {
        return Integer.parseInt(hexData, 16);
    }

    /**
     * 十六进制转换为ASCII格式编码
     *
     * @author Lizb
     * @date 2023/3/17 16:28:55
     */
    public static String hexToAscii(String hexData) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hexData.length(); i += 2) {
            String str = hexData.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    /**
     * 将十进制数转换为 保留两位的数, 0.00
     *
     * @author Lizb
     * @date 2023/3/20 17:24:37
     */
    public static String decToFloat(String decData) {
        float f = Float.parseFloat(decData) / 100;
        DecimalFormat df = new DecimalFormat("#0.00");
        String str = df.format(f);
        return str;
    }

    /**
     * 加油数据_加油时间, 格式整理
     *
     * @author Lizb
     * @date 2023/3/17 17:32:47
     */
    public static String gasDateHexToDec(String hexData) {
        // 年
        int year = hexToDec(hexData.substring(1, 4));
        // 月
        int mon = hexToDec(hexData.substring(4, 6));
        // 日
        int day = hexToDec(hexData.substring(6, 8)) - BASE_VALUE;
        // 时
        int hour = hexToDec(hexData.substring(8, 10)) - BASE_VALUE;
        // 分
        int min = hexToDec(hexData.substring(10, 12)) - BASE_VALUE;

        // 返回格式化后的数据
        return String.format("%04d-%02d-%02d %02d:%02d:00", year, mon, day, hour, min);
    }

    /**
     * 加油数据, 特殊格式中
     * 特征码05的油枪总量公式: 油枪总量=总油量1-总油量2
     *
     * @author Lizb
     * @date 2023/3/21 14:15:56
     */
    public static BigDecimal data05ToFormula(String num1, String num2) {
        BigDecimal number1 = new BigDecimal(num1);
        BigDecimal number2 = new BigDecimal(num2);
        return number1.subtract(number2);
    }
}
