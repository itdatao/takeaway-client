package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.Food;

import java.util.List;

/**
 * 餐品Mapper接口
 *
 * @author hht
 * @date 2021-01-27
 */
public interface FoodMapper extends BaseMapper<Food> {

    List<Food> getFoodAllBySale();
}
