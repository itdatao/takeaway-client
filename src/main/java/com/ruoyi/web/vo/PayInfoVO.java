package com.ruoyi.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @Author Huhuitao
 * @Date 2021/2/9 11:35
 */
@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@ApiModel("支付信息")
public class PayInfoVO implements Serializable {
    @ApiModelProperty("二维码URL")
    private String url;
    @ApiModelProperty("订单编号")
    private String orderId;
    @ApiModelProperty("商家名称")
    private String shopName;

}
