package com.zryy.bd_nettytcp_fordata.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import static com.zryy.bd_nettytcp_fordata.constant.CodeConstant.FunctionCode.BASE_VALUE;

/**
 * 自定义进制转换, 公式处理工具类
 *
 * @author Lizb
 * @version 1.0
 * @date 2023/3/17 13:46:45
 */
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
     *
     *
     * @author Lizb
     * @date 2023/3/23 16:21:14
     */
    public static String strDecToHex(int decimal){
        return Integer.toHexString(decimal);
    }

    /**
     * 十六进制转换为ASCII格式编码
     *
     * @author Lizb
     * @date 2023/3/17 16:28:55
     */
    public static String hexToAscii(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /**
     * ASCII转换十六进制字符串
     *
     * @param str 待转换的ASCII字符串
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     * @author Lizb
     * @date 2023/3/23 15:03:14
     */
    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder strBuilder = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (byte b : bs) {
            bit = (b & 0x0f0) >> 4;
            strBuilder.append(chars[bit]);
            bit = b & 0x0f;
            strBuilder.append(chars[bit]);
            strBuilder.append(' ');
        }
        return strBuilder.toString().trim();
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
     * String转换, 减法
     * 加油数据, 特殊格式中
     * 特征码05的油枪总量公式: 油枪总量=总油量1-总油量2
     *
     * @author Lizb
     * @date 2023/3/21 14:15:56
     */
    public static BigDecimal strToSubtraction(String num1, String num2) {
        BigDecimal number1 = new BigDecimal(num1);
        BigDecimal number2 = new BigDecimal(num2);
        return number1.subtract(number2);
    }

    /**
     * String转换, 加法
     *
     * @author Lizb
     * @date 2023/3/21 17:14:36
     */
    public static BigDecimal strToAddition(String num1, String num2) {
        BigDecimal number1 = new BigDecimal(num1);
        BigDecimal number2 = new BigDecimal(num2);
        return number1.add(number2);
    }

}
