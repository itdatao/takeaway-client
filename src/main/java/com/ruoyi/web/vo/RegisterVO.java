package com.ruoyi.web.vo;

import lombok.*;

/**
 * @Author Huhuitao
 * @Date 2021/2/5 14:24
 */
@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class RegisterVO {

    private String nickname;

    private String phone;
    //收货地址
    private String city;

    private Integer sex;

    private String password;

}
