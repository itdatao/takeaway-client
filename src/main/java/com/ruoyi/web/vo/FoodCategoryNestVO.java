package com.ruoyi.web.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Huhuitao
 * @Date 2021/1/26 15:11
 */
@Data
public class FoodCategoryNestVO {
    private String id;
    private String parentId;
    private String title;
    private List<FoodCategoryVO> children = new ArrayList<>();
}
