package com.ruoyi.web.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Huhuitao
 * @Date 2021/2/8 15:12
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_cart")
public class Cart implements Serializable {

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    //用户id
    private String userId;
    //餐品ID
    private String foodId;
    //餐品价格
    private BigDecimal foodPrice;
    //餐品数量
    private Integer amount;
    //餐品名称
    private String foodName;
    //餐品图片
    private String foodUrl;
    //餐品总价格 单价*数量
    private BigDecimal totalPrice;
    // 0 未选中 1选中
    private Integer checked;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDeleted;
}
