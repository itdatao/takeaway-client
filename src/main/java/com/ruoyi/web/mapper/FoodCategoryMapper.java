package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.FoodCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 餐品分类Mapper接口
 *
 * @author hht
 * @date 2021-01-25
 */
public interface FoodCategoryMapper extends BaseMapper<FoodCategory> {

    /**
     * 根据父类Id查询当前分类下的所有子类
     *
     * @param parentId
     * @return
     */
    List<FoodCategory> selectChildrenFoodCategoryByParentId(@Param("parentId") String parentId);

    /**
     * 判断该分类下是否存在子类
     *
     * @param categoryId
     * @return
     */
    int hasChildByCategoryId(@Param("categoryId") String categoryId);

    /**
     * 根据父类id校验分类名称是否合法
     *
     * @param categoryName
     * @param parentId
     * @return
     */
    FoodCategory checkCategoryNameUnique(@Param("categoryName") String categoryName, @Param("parentId") String parentId);


}
