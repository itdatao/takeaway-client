package com.ruoyi.web.controller;

import com.ruoyi.web.common.AjaxResult;
import com.ruoyi.web.domain.Cart;
import com.ruoyi.web.service.CartService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.List;

/**
 * 客户端购物车接口
 *
 * @Author Huhuitao
 * @Date 2021/2/3 15:22
 */
@CrossOrigin
@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    @Qualifier(value = "CartServiceImpl")
    private CartService cartService;

    //添加购物车 /cart/${params.productId}?quantity=${params.quantity}
    @ApiOperation("添加购物车")
    @PostMapping("/add/{productId}")
    public AjaxResult addCart(@PathVariable String productId,
                              @RequestParam("quantity") Integer quantity,
                              HttpServletRequest request){
        String token = request.getHeader("token");
        int rowCount = cartService.addCart(token, productId, quantity);

        return rowCount>0?AjaxResult.success():AjaxResult.error("添加失败");
    }

    //清空购物车
    @ApiOperation("清空购物车")
    @DeleteMapping("/clear")
    public AjaxResult clearCart(HttpServletRequest request){
        String token = request.getHeader("token");
        cartService.clearCart(token);
        return AjaxResult.success();
    }

    //删除购物车 指定餐品
    @ApiOperation("删除购物车指定商品")
    @DeleteMapping("/{cartId}")
    public AjaxResult deleteCart(@PathVariable("cartId") String cartId){
        int rowCount = cartService.deleteCart(cartId);
        return rowCount>0?AjaxResult.success():AjaxResult.error();
    }

    //查询当前用户购物车所有餐品 get('/cart/list')
    @ApiOperation("购物车列表")
    @GetMapping("/list")
    public AjaxResult getCartList(HttpServletRequest request){
        String token = request.getHeader("token");
        List<Cart> cart = cartService.getCart(token);
        return AjaxResult.success(cart);
    }

    @ApiOperation("修改数量")
    @PutMapping("/{cartId}/{amount}")
    public AjaxResult updateCartAccount(@PathVariable String cartId,@PathVariable Integer amount){
        int rowCount = cartService.updateCartCount(cartId, amount);
        return rowCount>0?AjaxResult.success():AjaxResult.error();
    }

}
