package com.ruoyi.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.web.domain.OrderItem;
import com.ruoyi.web.domain.Shop;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Huhuitao
 * @Date 2021/1/31 11:41
 */
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class OrderVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /*订单ID*/
    private String orderId;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 收货地址
     */
    private String address;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 备注
     */
    private String remark;
    /**
     * 餐品总价
     */
    private BigDecimal foodsPrice;

    /**
     * 订单总费用
     */
    private BigDecimal orderPrice;
    /**
     * 支付方式
     */
    private String payMethod;
    /**
     * 支付状态
     */
    private Integer payStatus;
    /**
     * 交易时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;
    /*订单创建时间*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 商家信息
     */
    private Shop shop;
    /*订单项列表*/
    private List<OrderItemVO> orderItems = new ArrayList<>();

}
