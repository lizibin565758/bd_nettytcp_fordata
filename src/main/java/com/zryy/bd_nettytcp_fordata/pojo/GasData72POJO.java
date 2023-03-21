package com.zryy.bd_nettytcp_fordata.pojo;

import lombok.*;

import java.io.Serializable;

/**
 * 加油数据(72字节)
 *
 * @author Lizb
 * @version 1.0
 * @date 2023/3/20 13:38:08
 */
@Data
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class GasData72POJO extends BasePOJO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 油枪号码_1B:第6字节 （⼗进制）
     */
    private int oilGunCode;

    /**
     * 加油时间_6B:第7-12字节 TODO (解析为⼗进制，精确到分；⽇、时、分解析⼗进制后需要减去20，防⽌处理粘包时数据拆分)
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
     * 备用1_2B
     */
    private String reserve1;

    /**
     * 备用2_2B
     */
    private String reserve2;

    /**
     * 当前油枪总油量_12B:第43-54字节
     */
    private String totalOilQuantityOfOilGun;

    /**
     * 备用3_12B
     */
    private String reserve3;

    /**
     * 帧号_1B:第67字节
     */
    private String frameNumber;

    /**
     * crc校验_2B: (默认00) 第68-69 字节 (crc16⾼位在前，低位在后)
     */
    private String crcCheck;


}
