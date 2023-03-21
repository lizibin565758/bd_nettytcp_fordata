package com.zryy.bd_nettytcp_fordata.pojo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 特殊加密加油机数据(96字节)
 *
 * @author Lizb
 * @version 1.0
 * @date 2023/3/20 14:08:39
 */
@Data
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class GasData96POJO extends BasePOJO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 油枪号码_1B:第6字节
     */
    private int oilGunCode;

    /**
     * 加油时间_6B:第7-12字节
     */
    private String refuelingTime;

    /**
     * 加油量_10B:第13-22字节
     */
    private String fuelQuantity;

    /**
     * 加油金额_10B:第23-32字节
     */
    private String refuelingAmount;

    /**
     * 单价_6B:第33-38字节
     */
    private String unitPrice;

    /**
     * 备用1_2B:第39-40字节
     */
    private String reserve1;

    /**
     * 特征码_1B:第41字节
     */
    private String signatureCode;

    /**
     * 备用2_1B:(默认0):第42字节
     */
    private String reserve2;

    /**
     * 相对总油量1_12B:第43-54字节
     */
    private String relativeTotalOilQuantity1;

    /**
     * 相对总油量2_12B:第55-66字节
     */
    private String relativeTotalOilQuantity2;

    /**
     * 特征码为05的油枪总油量: 油枪总量 = 总油量1 - 总油量2
     */
    private String totalOilQuantityOfOilGun05;

    /**
     * 相对总金额1_12B:第67-78字节
     */
    private String relativeTotalAmount1;

    /**
     * 相对总金额2_12B:第79-90字节
     */
    private String relativeTotalAmount2;

    /**
     * 帧号_1B:第91字节
     */
    private String frameNumber;

    /**
     * crc校验_2B: (默认00) 第92-93 字节 （crc16⾼位在前，低位在后）
     */
    private String crcCheck;


}
