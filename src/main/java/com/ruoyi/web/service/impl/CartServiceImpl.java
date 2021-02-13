package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.domain.Cart;
import com.ruoyi.web.domain.Food;
import com.ruoyi.web.mapper.CartMapper;
import com.ruoyi.web.mapper.FoodMapper;
import com.ruoyi.web.service.CartService;
import com.ruoyi.web.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author Huhuitao
 * @Date 2021/2/8 15:28
 */
@Service("CartServiceImpl")
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Autowired
    private FoodMapper foodMapper;

    /**
     * 购物车列表
     * @param token
     * @return
     * @throws ExpiredJwtException
     */
    @Override
    public List<Cart> getCart(String token) throws ExpiredJwtException {
        String userId = JwtUtil.getSubject(token, "id");
        QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("user_id", userId);
        return baseMapper.selectList(cartQueryWrapper);
    }

    /**
     * 增加餐品到购物车
     * @param token
     * @param foodId
     * @param count
     * @return
     * @throws ExpiredJwtException
     */
    @Override
    public int addCart(String token, String foodId, Integer count) throws ExpiredJwtException {
        String userId = JwtUtil.getSubject(token, "id");
        Food food = foodMapper.selectById(foodId);
        //将food转传承Cart
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setAmount(count);
        cart.setFoodId(food.getId());
        cart.setFoodName(food.getName());
        cart.setFoodPrice(food.getPrice());
        cart.setFoodUrl(food.getCover());
        cart.setTotalPrice(new BigDecimal(food.getPrice().doubleValue() * count));

        return baseMapper.insert(cart);
    }

    /**
     * 清空购物车
     * @param token
     * @throws ExpiredJwtException
     */
    @Override
    public void clearCart(String token) throws ExpiredJwtException{
        String userId = JwtUtil.getSubject(token, "id");
        UpdateWrapper<Cart> cartUpdateWrapper = new UpdateWrapper<>();
        cartUpdateWrapper.eq("user_id", userId);
        baseMapper.delete(cartUpdateWrapper);
    }

    /**
     * 当该餐品的数量在购物车中为0时删除
     * 删除指定餐品
     * @param cartId
     * @return
     */
    @Override
    public int deleteCart(String cartId) {
        return baseMapper.deleteById(cartId);
    }

    /**
     * 修改购物车中单个商品的数量
     * @param cartId
     * @param amount 该餐品数
     * @return
     */
    @Override
    public int updateCartCount(String cartId, Integer amount) {
        Cart cart = baseMapper.selectById(cartId);
        BigDecimal foodPrice = cart.getFoodPrice();
        double price = foodPrice.doubleValue()*amount;
        BigDecimal totalPrice = new BigDecimal(price);
        //更新餐品数量 和 餐品总价
        cart.setAmount(amount);
        cart.setTotalPrice(totalPrice);
        return baseMapper.updateById(cart);
    }
}
