package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.Food;

import java.util.List;

/**
 * 餐品Service接口
 *
 * @author hht
 * @date 2021-01-27
 */
public interface IFoodService {

    /**
     * 查询列表
     */
    List<Food> queryList(Food food);


    List<Food> getFoodListByCategory(String categoryId);

    List<Food> getFoodAll();
}
