package com.zryy.bd_nettytcp_fordata.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.zryy.bd_nettytcp_fordata.constant.CodeConstant;
import com.zryy.bd_nettytcp_fordata.pojo.GasData72POJO;
import com.zryy.bd_nettytcp_fordata.pojo.GasData96POJO;
import com.zryy.bd_nettytcp_fordata.service.HexToAllFormatService;
import com.zryy.bd_nettytcp_fordata.utils.CrossoverToolUtils;
import com.zryy.bd_nettytcp_fordata.utils.DataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.zryy.bd_nettytcp_fordata.constant.CodeConstant.FunctionCode.*;
import static com.zryy.bd_nettytcp_fordata.utils.CrossoverToolUtils.data05ToFormula;
import static com.zryy.bd_nettytcp_fordata.utils.CrossoverToolUtils.hexToDec;

/**
 * 处理接收的参数_具体逻辑
 *
 * @author Lizb
 * @version 1.0
 * @date 2023/3/20 10:05:48
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HexToAllFormatImpl implements HexToAllFormatService {

    @Override
    public void hexToCutOut(String msg) {
        // 分化逻辑
        hexCutOutFunctionCode(msg);
    }

    /**
     * 截取功能码, 利用报文功能码分发逻辑方法
     *
     * @author Lizb
     * @date 2023/3/20 13:33:02
     */
    private void hexCutOutFunctionCode(String msg) {
        String functionCode = msg.substring(10, 12); // 判断报文种类
        switch (functionCode) {
            // 注册包
            case SIGNCODE:

                break;
            // 心跳包
            case HEARTBEATCODE:
                log.info("心跳报文, 已自动回复上位机");
                break;
            // 油枪数据
            case REFUELINGDATA:
                if (msg.length() > CODELENGTH) {
                    log.info("🚀 特殊加密加油机报文");
                    gasData96ToCutOut(msg);
                } else if (msg.length() == CODELENGTH) {
                    log.info("🚀 正常加油机报文");
                    gasData72ToCutOut(msg);
                }
                break;
            default:
                // 其他情况的处理
                break;
        }
    }

    /**
     * 加油_数据格式⼀(72字节)
     *
     * @author Lizb
     * @date 2023/3/20 13:35:44
     */
    private void gasData72ToCutOut(String msg) {
        StringBuffer msgStrBuffer = new StringBuffer(msg);
        GasData72POJO gasData72POJO = new GasData72POJO();

        // 起始标志
        gasData72POJO.setStartingSymbol(msgStrBuffer.substring(0, 10));
        // 功能码
        gasData72POJO.setFunctionCode(msgStrBuffer.substring(10, 12));
        // 加油枪号码
        gasData72POJO.setOilGunCode(hexToDec(msgStrBuffer.substring(12, 14)));
        // 加油时间
        gasData72POJO.setRefuelingTime(CrossoverToolUtils.gasDateHexToDec(msgStrBuffer.substring(14, 26)));
        // 加油量
        gasData72POJO.setFuelQuantity(CrossoverToolUtils.decToFloat(CrossoverToolUtils.hexToAscii(msgStrBuffer.substring(26, 46))));
        // 加油金额
        gasData72POJO.setRefuelingAmount(CrossoverToolUtils.decToFloat(CrossoverToolUtils.hexToAscii(msgStrBuffer.substring(46, 66))));
        // 加油单价
        gasData72POJO.setUnitPrice(CrossoverToolUtils.decToFloat(CrossoverToolUtils.hexToAscii(msgStrBuffer.substring(66, 72))));
        // 备用1 不做处理, 直接转10进制(String类型)
        gasData72POJO.setReserve1(String.valueOf(hexToDec(msgStrBuffer.substring(72, 76))));
        // 备用2 不做处理, 直接转10进制(String类型)
        gasData72POJO.setReserve2(String.valueOf(hexToDec(msgStrBuffer.substring(76, 80))));
        // 加油机累计加油总量
        gasData72POJO.setTotalOilQuantityOfOilGun(CrossoverToolUtils.decToFloat(CrossoverToolUtils.hexToAscii(msgStrBuffer.substring(80, 104))));
        // 备用3 不做处理, 直接转10进制(String类型)
        gasData72POJO.setReserve3(msgStrBuffer.substring(104, 128));
        // 帧号
        gasData72POJO.setFrameNumber(String.valueOf(hexToDec(msgStrBuffer.substring(128, 130))));
        // CRC校验
        gasData72POJO.setCrcCheck(String.valueOf(hexToDec(msgStrBuffer.substring(130, 134))));
        // 结束标志
        gasData72POJO.setEndCode(String.valueOf(hexToDec(msgStrBuffer.substring(134, 138))));

        // 将数据转换为Json格式, 方便调用, TODO 也可不转, 注掉即可
        String gasData72JSON = JSON.toJSONString(gasData72POJO);
        System.out.println(" 🚀 " + DataUtils.formatTimeYMD_HMS_SSS(System.currentTimeMillis()) + "===>>> JSON: " + gasData72JSON);
    }

    /**
     * 加油数据_
     *
     * @author Lizb
     * @date 2023/3/21 09:16:27
     */
    public void gasData96ToCutOut(String msg) {
        StringBuffer msgStrBuffer = new StringBuffer(msg);
        GasData96POJO gasData96POJO = new GasData96POJO();
        /*
        TODO 特征码说明:
        05: 此特征码数据,⽤于后⾯加油数据油枪总量计算: 油枪总量=总油量1-总油量2
        01/02/03/04: 此特征码数据:
        加油量=总油量1-总油量2;
        加油⾦额=总⾦额1-总⾦额2;
        加油总量=加油量+原加油总量;
        设备注册后如有加油数据会⾸先发送05特征码加油数据
        */
        // 获取特征码, 走不同逻辑
        String signatureCode = msgStrBuffer.substring(82, 84);
        System.out.println(" 🚀 " + DataUtils.formatTimeYMD_HMS_SSS(System.currentTimeMillis()) + "===>>> 特征码为: " + signatureCode);

        switch (signatureCode) {
            case featureCode01:
                gasData96POJO = featureCode01forData(gasData96POJO, msgStrBuffer, signatureCode);
                break;
            case featureCode02:
                gasData96POJO = featureCode02forData(gasData96POJO, msgStrBuffer, signatureCode);
                break;
            case featureCode03:
                gasData96POJO = featureCode03forData(gasData96POJO, msgStrBuffer, signatureCode);
                break;
            case featureCode04:
                gasData96POJO = featureCode04forData(gasData96POJO, msgStrBuffer, signatureCode);
                break;
            case featureCode05:
                gasData96POJO = featureCode05forData(gasData96POJO, msgStrBuffer, signatureCode);
                break;
            default:
                break;
        }
        String gasData96JSON = JSON.toJSONString(gasData96POJO);
        System.out.println(" 🚀 " + DataUtils.formatTimeYMD_HMS_SSS(System.currentTimeMillis()) + "===>>> gasData96JSON: " + gasData96JSON);
    }

    /**
     * 特殊格式加油数据
     * 特征码为 01 时调用
     *
     * @author Lizb
     * @date 2023/3/21 14:33:47
     */
    public GasData96POJO featureCode01forData(GasData96POJO gasData96POJO, StringBuffer msgStrBuffer, String signatureCode){
        // 起始标志
        gasData96POJO.setStartingSymbol(msgStrBuffer.substring(0, 10));
        // 功能码
        gasData96POJO.setFunctionCode(msgStrBuffer.substring(10, 12));
        // 加油枪号码
        gasData96POJO.setOilGunCode(hexToDec(msgStrBuffer.substring(12, 14)));
        // 加油时间
        gasData96POJO.setRefuelingTime(CrossoverToolUtils.gasDateHexToDec(msgStrBuffer.substring(14, 26)));
        // 加油量
        gasData96POJO.setFuelQuantity(CrossoverToolUtils.decToFloat(CrossoverToolUtils.hexToAscii(msgStrBuffer.substring(26, 46))));
        // 加油金额
        gasData96POJO.setRefuelingAmount(CrossoverToolUtils.decToFloat(CrossoverToolUtils.hexToAscii(msgStrBuffer.substring(46, 66))));
        // 加油单价
        gasData96POJO.setUnitPrice(CrossoverToolUtils.decToFloat(CrossoverToolUtils.hexToAscii(msgStrBuffer.substring(66, 78))));
        // 备用1
        gasData96POJO.setReserve1(msgStrBuffer.substring(78, 82));
        // 特征码
        gasData96POJO.setSignatureCode(signatureCode);
        // 相对总油量1
        String relativeTotalOilQuantity1 = CrossoverToolUtils.decToFloat(CrossoverToolUtils.hexToAscii(msgStrBuffer.substring(86, 110)));
        gasData96POJO.setRelativeTotalOilQuantity1(relativeTotalOilQuantity1);
        // 相对总油量2
        String relativeTotalOilQuantity2 = CrossoverToolUtils.decToFloat(CrossoverToolUtils.hexToAscii(msgStrBuffer.substring(110, 134)));
        gasData96POJO.setRelativeTotalOilQuantity2(relativeTotalOilQuantity2);
        // 05特征码的油枪总量, 公式: 油枪总量=总油量1-总油量2
        gasData96POJO.setTotalOilQuantityOfOilGun05(String.valueOf(data05ToFormula(relativeTotalOilQuantity1, relativeTotalOilQuantity2)));
        // 相对金额1
        gasData96POJO.setRelativeTotalAmount1(CrossoverToolUtils.decToFloat(CrossoverToolUtils.hexToAscii(msgStrBuffer.substring(134, 158))));
        // 相对金额2
        gasData96POJO.setRelativeTotalAmount2(CrossoverToolUtils.decToFloat(CrossoverToolUtils.hexToAscii(msgStrBuffer.substring(158, 182))));
        // 帧号
        gasData96POJO.setFrameNumber(String.valueOf(hexToDec(msgStrBuffer.substring(182, 184))));
        // CRC校验
        gasData96POJO.setCrcCheck(String.valueOf(Integer.parseInt(msgStrBuffer.substring(184, 188), 16)));
        // 结束标志
        gasData96POJO.setEndCode(String.valueOf(Integer.parseInt(msgStrBuffer.substring(188, 192), 16)));

        return gasData96POJO;
    }

    /**
     * 特殊格式加油数据
     * 特征码为 02 时调用
     *
     * @author Lizb
     * @date 2023/3/21 14:33:47
     */
    public GasData96POJO featureCode02forData(GasData96POJO gasData96POJO, StringBuffer msgStrBuffer, String signatureCode){

        return gasData96POJO;
    }

    /**
     * 特殊格式加油数据
     * 特征码为 03 时调用
     *
     * @author Lizb
     * @date 2023/3/21 14:33:47
     */
    public GasData96POJO featureCode03forData(GasData96POJO gasData96POJO, StringBuffer msgStrBuffer, String signatureCode){

        return gasData96POJO;
    }

    /**
     * 特殊格式加油数据
     * 特征码为 04 时调用
     *
     * @author Lizb
     * @date 2023/3/21 14:33:47
     */
    public GasData96POJO featureCode04forData(GasData96POJO gasData96POJO, StringBuffer msgStrBuffer, String signatureCode){

        return gasData96POJO;
    }

    /**
     * 特殊格式加油数据
     * 特征码为 05 时调用的方法
     *
     * @author Lizb
     * @date 2023/3/21 14:09:33
     */
    public GasData96POJO featureCode05forData(GasData96POJO gasData96POJO, StringBuffer msgStrBuffer, String signatureCode) {
        // 起始标志
        gasData96POJO.setStartingSymbol(msgStrBuffer.substring(0, 10));
        // 功能码
        gasData96POJO.setFunctionCode(msgStrBuffer.substring(10, 12));
        // 加油枪号码
        gasData96POJO.setOilGunCode(hexToDec(msgStrBuffer.substring(12, 14)));
        // 加油时间
        gasData96POJO.setRefuelingTime(CrossoverToolUtils.gasDateHexToDec(msgStrBuffer.substring(14, 26)));
        // 加油量
        gasData96POJO.setFuelQuantity(CrossoverToolUtils.decToFloat(CrossoverToolUtils.hexToAscii(msgStrBuffer.substring(26, 46))));
        // 加油金额
        gasData96POJO.setRefuelingAmount(CrossoverToolUtils.decToFloat(CrossoverToolUtils.hexToAscii(msgStrBuffer.substring(46, 66))));
        // 加油单价
        gasData96POJO.setUnitPrice(CrossoverToolUtils.decToFloat(CrossoverToolUtils.hexToAscii(msgStrBuffer.substring(66, 78))));
        // 备用1
        gasData96POJO.setReserve1(msgStrBuffer.substring(78, 82));
        // 特征码
        gasData96POJO.setSignatureCode(signatureCode);
        // 相对总油量1
        String relativeTotalOilQuantity1 = CrossoverToolUtils.decToFloat(CrossoverToolUtils.hexToAscii(msgStrBuffer.substring(86, 110)));
        gasData96POJO.setRelativeTotalOilQuantity1(relativeTotalOilQuantity1);
        // 相对总油量2
        String relativeTotalOilQuantity2 = CrossoverToolUtils.decToFloat(CrossoverToolUtils.hexToAscii(msgStrBuffer.substring(110, 134)));
        gasData96POJO.setRelativeTotalOilQuantity2(relativeTotalOilQuantity2);
        // 05特征码的油枪总量, 公式: 油枪总量=总油量1-总油量2
        gasData96POJO.setTotalOilQuantityOfOilGun05(String.valueOf(data05ToFormula(relativeTotalOilQuantity1, relativeTotalOilQuantity2)));
        // 相对金额1
        gasData96POJO.setRelativeTotalAmount1(CrossoverToolUtils.decToFloat(CrossoverToolUtils.hexToAscii(msgStrBuffer.substring(134, 158))));
        // 相对金额2
        gasData96POJO.setRelativeTotalAmount2(CrossoverToolUtils.decToFloat(CrossoverToolUtils.hexToAscii(msgStrBuffer.substring(158, 182))));
        // 帧号
        gasData96POJO.setFrameNumber(String.valueOf(hexToDec(msgStrBuffer.substring(182, 184))));
        // CRC校验
        gasData96POJO.setCrcCheck(String.valueOf(Integer.parseInt(msgStrBuffer.substring(184, 188), 16)));
        // 结束标志
        gasData96POJO.setEndCode(String.valueOf(Integer.parseInt(msgStrBuffer.substring(188, 192), 16)));

        return gasData96POJO;
    }


}
