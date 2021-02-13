package com.ruoyi.web.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
public class PayResultDTO implements Serializable {
    //支付ID
    private Long payId;
    //第三方支付方式
    private String thirdPartId;
    // 支付状态
    private Integer status;
    // 支付金额
    private BigDecimal amount;
    // 支付信息
    private String msg;
}