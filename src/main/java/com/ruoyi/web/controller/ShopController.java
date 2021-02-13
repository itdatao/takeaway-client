package com.ruoyi.web.controller;

import com.ruoyi.web.common.AjaxResult;
import com.ruoyi.web.domain.Shop;
import com.ruoyi.web.service.ShopService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Huhuitao
 * @Date 2021/2/4 11:58
 */
@CrossOrigin
@RequestMapping("/shop")
@RestController
public class ShopController {

    @Autowired
    @Qualifier("ShopServiceImpl")
    private ShopService shopService;

    @ApiOperation("根据id获取商家信息")
    @RequestMapping("/info/{id}")
    public AjaxResult getShop(@PathVariable String id){
        Shop shopInfo = shopService.getShopInfo(id);
        return AjaxResult.success(shopInfo);
    }

}
