package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.domain.Food;
import com.ruoyi.web.domain.StaticFile;
import com.ruoyi.web.mapper.FoodMapper;
import com.ruoyi.web.service.FileService;
import com.ruoyi.web.service.IFoodService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 餐品Service业务层处理
 *
 * @author hht
 * @date 2021-01-27
 */
@Primary
@Service
public class FoodServiceImpl extends ServiceImpl<FoodMapper, Food> implements IFoodService {


    @Override
    public List<Food> queryList(Food food) {
        LambdaQueryWrapper<Food> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(food.getName())) {
            lqw.like(Food::getName, food.getName());
        }
        if (StringUtils.isNotBlank(food.getCategoryId())) {
            lqw.eq(Food::getCategoryId, food.getCategoryId());
        }

        if (food.getScore() != null) {
            lqw.eq(Food::getScore, food.getScore());
        }

        if (food.getStatus() != null) {
            lqw.eq(Food::getStatus, food.getStatus());
        }
        return this.list(lqw);
    }


    @Override
    public List<Food> getFoodAll() {
        return baseMapper.getFoodAllBySale();
    }

    @Override
    public List<Food> getFoodListByCategory(String categoryId) {
        if (StringUtils.isBlank(categoryId)){
            return baseMapper.selectList(null);
        }
        QueryWrapper<Food> foodQueryWrapper = new QueryWrapper<>();
        foodQueryWrapper.eq("category_id", categoryId);
        return baseMapper.selectList(foodQueryWrapper);
    }


    private boolean checkFoodNameUnique(String foodName) {
        QueryWrapper<Food> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", foodName);
        Food food = baseMapper.selectOne(queryWrapper);
        return food == null;

    }

}
