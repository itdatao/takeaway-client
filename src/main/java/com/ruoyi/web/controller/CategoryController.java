package com.ruoyi.web.controller;


import com.ruoyi.web.common.AjaxResult;
import com.ruoyi.web.domain.FoodCategory;
import com.ruoyi.web.service.IFoodCategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 客户端餐品分类接口
 *
 * @Author Huhuitao
 * @Date 2021/2/3 15:22
 */
@CrossOrigin
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    @Qualifier("FoodCategoryServiceImpl")
    private IFoodCategoryService categoryService;

    @ApiOperation("获取所有分类")
    @GetMapping("/list")
    public AjaxResult getAllList() {
        List<FoodCategory> categories = categoryService.list();
        return AjaxResult.success(categories);
    }


}
