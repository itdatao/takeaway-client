package com.ruoyi.web.vo;

import lombok.Data;

/**
 * @Author Huhuitao
 * @Date 2021/1/26 15:09
 */
@Data
public class FoodCategoryVO {
    public FoodCategoryVO() {
    }

    public FoodCategoryVO(String id, String title) {
        this.id = id;
        this.title = title;
    }

    private String id;

    private String title;

}
