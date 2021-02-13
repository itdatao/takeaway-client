package com.ruoyi.web.controller;

import com.ruoyi.web.common.AjaxResult;
import com.ruoyi.web.domain.Food;
import com.ruoyi.web.service.IFoodService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户端餐品接口
 *
 * @Author Huhuitao
 * @Date 2021/2/3 15:17
 */
@CrossOrigin
@RestController
@RequestMapping("/food")
public class FoodController {
    @Autowired
    private IFoodService foodService;

    @GetMapping("/list")
    @ApiOperation("显示餐品列表")
    public AjaxResult getAll() {
        List<Food> foods = foodService.getFoodAll();
        return AjaxResult.success(foods);
    }


    @ApiOperation("根据分类查询餐品")
    @GetMapping("/{categoryId}")
    public AjaxResult foodListByCategory(@PathVariable String categoryId) {

        List<Food> foods = foodService.getFoodListByCategory(categoryId);
        return AjaxResult.success(foods);
    }


}
