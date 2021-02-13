package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.FoodCategory;
import com.ruoyi.web.vo.FoodCategoryNestVO;
import com.ruoyi.web.vo.FoodCategoryVO;


import java.util.List;

/**
 * 餐品分类Service接口
 *
 * @author hht
 * @date 2021-01-25
 */
public interface IFoodCategoryService  {

    /**
     * 查询列表
     */
    List<FoodCategory> queryList(FoodCategory foodCategory);

    /**
     * 查询分类列表
     *
     * @return
     */
    List<FoodCategoryNestVO> nestedList();

    List<FoodCategoryVO> queryFirst();

    boolean hasChildCategory(String categoryId);

    List<FoodCategoryVO> listAll();

    List<FoodCategory> list();
}
