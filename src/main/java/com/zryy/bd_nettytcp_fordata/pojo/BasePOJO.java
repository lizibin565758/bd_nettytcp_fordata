package com.zryy.bd_nettytcp_fordata.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 报文实体_基类
 *
 * @author Lizb
 * @version 1.0
 * @date 2023/3/20 17:17:36
 */
@Data
public class BasePOJO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 起始标志
     */
    private String startingSymbol;

    /**
     * 功能码
     */
    private String functionCode;

    /**
     * 结束标志_2B: (0D 0A) 第70-71
     */
    private String endCode;


}
