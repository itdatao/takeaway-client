package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.domain.Shop;
import com.ruoyi.web.mapper.ShopMapper;
import com.ruoyi.web.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author Huhuitao
 * @Date 2021/2/2 22:40
 */
@Service("ShopServiceImpl")
@Slf4j
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements ShopService {

    public Shop getShopInfo(String sellId) {
        return baseMapper.selectById(sellId);
    }

}
