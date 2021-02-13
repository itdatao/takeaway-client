package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.Cart;

import java.util.List;

/**
 * @Author Huhuitao
 * @Date 2021/2/8 15:19
 */
public interface CartService {

    public List<Cart> getCart(String token);

    public int addCart(String token,String foodId,Integer count);

    public void clearCart(String token);

    public int deleteCart(String cartId);

    public int updateCartCount(String cartId,Integer amount);

}
