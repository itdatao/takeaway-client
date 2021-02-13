package com.ruoyi.web.vo;

import lombok.*;

import java.io.Serializable;

/**
 * @Author Huhuitao
 * @Date 2021/2/5 16:06
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserVO implements Serializable {
    private String id;
    private String nickname;
    private String phone;
    private String avatar;
    private Integer sex;
    private String city;

}
