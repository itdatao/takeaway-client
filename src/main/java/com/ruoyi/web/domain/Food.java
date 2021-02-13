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
import java.util.HashMap;
import java.util.Map;

/**
 * 餐品对象 t_food
 *
 * @author hht
 * @date 2021-01-27
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_food")
public class Food implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 餐品id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 餐品名称
     */
    private String name;

    /**
     * 餐品描述
     */
    private String foodDescribe;

    /**
     * 销量
     */
    private Long sale;

    /**
     * 所属分类
     */
    private String categoryId;

    /**
     * 图片url
     */
    private String cover;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 评分
     */
    private Integer score;

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

    /**
     * 餐品状态
     */
    private Integer status;

    @TableField(exist = false)
    private Map<String, Object> params = new HashMap<>();
}
