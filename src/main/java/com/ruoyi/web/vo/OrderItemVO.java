package com.ruoyi.web.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Huhuitao
 * @Date 2021/2/11 9:49
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemVO implements Serializable {
    // detailId
    private String productId;
    // 所属订单ID
    private String orderId;
    // 图片
    private String foodUrl;
    private BigDecimal foodPrice;
    private String foodName;
    // 数量
    private Integer amount;
    // 总价
    private BigDecimal total;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


}
