package com.ruoyi.web.form;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author Huhuitao
 * @Date 2021/2/8 23:20
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CartForm {
    //餐品ID
    private String productId;
    //餐品数量
    private Integer quantity;
}
