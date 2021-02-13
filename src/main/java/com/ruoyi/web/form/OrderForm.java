package com.ruoyi.web.form;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Huhuitao
 * @Date 2021/2/8 23:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class OrderForm {
    //商家ID
    private String shopId;
    //用户ID
    private String userId;
    //用户备注
    private String remark;

    private List<CartForm> productList = new ArrayList<>();
}
