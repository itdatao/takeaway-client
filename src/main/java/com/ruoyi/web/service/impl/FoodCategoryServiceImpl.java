package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ruoyi.web.domain.FoodCategory;
import com.ruoyi.web.mapper.FoodCategoryMapper;
import com.ruoyi.web.service.IFoodCategoryService;
import com.ruoyi.web.vo.FoodCategoryNestVO;
import com.ruoyi.web.vo.FoodCategoryVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 餐品分类Service业务层处理
 *
 * @author hht
 * @date 2021-01-25
 */
@Service("FoodCategoryServiceImpl")
public class FoodCategoryServiceImpl extends ServiceImpl<FoodCategoryMapper, FoodCategory> implements IFoodCategoryService {

    @Autowired
    private FoodCategoryMapper foodCategoryMapper;

    @Override
    public List<FoodCategory> queryList(FoodCategory foodCategory) {
        LambdaQueryWrapper<FoodCategory> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(foodCategory.getTitle())) {
            lqw.like(FoodCategory::getTitle, foodCategory.getTitle());
        }
        return this.list(lqw);
    }



    /**
     * 查询所有分类封装到FoodCategoryVO
     *
     * @return
     */
    @Override
    public List<FoodCategoryVO> listAll() {
        List<FoodCategory> categories = baseMapper.selectList(null);
        List<FoodCategoryVO> foodCategoryVOS = categories.stream().map(c -> {
            return new FoodCategoryVO(c.getId(), c.getTitle());
        }).collect(Collectors.toList());
        return foodCategoryVOS;
    }

    /**
     * 判断是否有子类
     *
     * @param categoryId
     * @return
     */
    @Override
    public boolean hasChildCategory(String categoryId) {
        System.out.println(categoryId);
        if (StringUtils.isBlank(categoryId)) {
            throw new RuntimeException("参数错误");
        }
        int result = foodCategoryMapper.hasChildByCategoryId(categoryId);
        return result > 0;
    }



    /**
     * 根据分类名称查询餐品分类是否唯一
     *
     * @param title
     * @return
     */
    private boolean checkTitleUnique(String title) {
        QueryWrapper<FoodCategory> foodCategoryQueryWrapper = new QueryWrapper<>();
        foodCategoryQueryWrapper.eq("title", title);
        FoodCategory foodCategory = baseMapper.selectOne(foodCategoryQueryWrapper);
        return foodCategory == null;
    }


    @Override
    public List<FoodCategoryVO> queryFirst() {
        QueryWrapper<FoodCategory> foodCategoryQueryWrapper = new QueryWrapper<>();
        foodCategoryQueryWrapper.eq("parent_id", 0);
        List<FoodCategory> foodCategories = baseMapper.selectList(foodCategoryQueryWrapper);

        return foodCategories.stream()
                .map(f -> new FoodCategoryVO(f.getId(), f.getTitle()))
                .collect(Collectors.toList());
    }

    /**
     * 查询分类列表
     *
     * @return
     */
    @Override
    public List<FoodCategoryNestVO> nestedList() {
        //最终要的到的数据列表
        ArrayList<FoodCategoryNestVO> subjectNestedVoArrayList = new ArrayList<>();

        //获取一级分类数据记录
        QueryWrapper<FoodCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", 0);
        queryWrapper.orderByAsc("sort", "id");
        List<FoodCategory> subjects = baseMapper.selectList(queryWrapper);

        //获取二级分类数据记录
        QueryWrapper<FoodCategory> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.ne("parent_id", 0);
        queryWrapper2.orderByAsc("sort", "id");
        List<FoodCategory> subSubjects = baseMapper.selectList(queryWrapper2);

        //填充一级分类vo数据
        int count = subjects.size();
        for (int i = 0; i < count; i++) {
            FoodCategory subject = subjects.get(i);

            //创建一级类别vo对象
            FoodCategoryNestVO subjectNestedVo = new FoodCategoryNestVO();
            BeanUtils.copyProperties(subject, subjectNestedVo);
            subjectNestedVoArrayList.add(subjectNestedVo);

            //填充二级分类vo数据
            ArrayList<FoodCategoryVO> subjectVoArrayList = new ArrayList<>();

            for (FoodCategory subSubject : subSubjects) {

                if (subject.getId().equals(subSubject.getParentId())) {

                    //创建二级类别vo对象
                    FoodCategoryVO subjectVo = new FoodCategoryVO();
                    BeanUtils.copyProperties(subSubject, subjectVo);
                    subjectVoArrayList.add(subjectVo);
                }
            }
            subjectNestedVo.setChildren(subjectVoArrayList);
        }
        return subjectNestedVoArrayList;

    }


    @Override
    public List<FoodCategory> list() {
        return baseMapper.selectList(null);
    }
}
