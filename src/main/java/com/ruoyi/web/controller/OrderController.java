package com.ruoyi.web.controller;

import com.ruoyi.web.common.AjaxResult;
import com.ruoyi.web.domain.Order;
import com.ruoyi.web.form.OrderForm;
import com.ruoyi.web.service.IOrderService;
import com.ruoyi.web.vo.OrderVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 客户端订单接口
 *
 * @Author Huhuitao
 * @Date 2021/2/3 15:22
 */
@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    @Qualifier("OrderServiceImpl")
    private IOrderService orderService;

    @ApiOperation("下单")
    @PostMapping
    public AjaxResult placeOrder(@RequestBody OrderForm orderForm){

        String orderId = orderService.placeOrder(orderForm);

        //返回订单id
        return AjaxResult.success("下单成功",orderId);
    }

    @ApiOperation("取消订单")
    @PutMapping("cancel/{orderId}")
    public AjaxResult cancelOrder(@PathVariable String orderId){
        int rowCount = orderService.cancelOrder(orderId);
        return rowCount>0?AjaxResult.success("取消订单成功"):AjaxResult.error("取消订单失败");
    }

    @ApiOperation("获取订单列表")
    @GetMapping("/list")
    public AjaxResult getOrderList( @RequestParam(value = "page",defaultValue = "1") Integer page,
                                    @RequestParam(value = "size",defaultValue = "10") Integer size,
                                    HttpServletRequest request){

        String token = request.getHeader("token");
        List<OrderVO> orders = orderService.getOrderList(token,page,size);
        return AjaxResult.success("订单列表",orders);
    }

    @ApiOperation("获取订单详情")
    @GetMapping("/{orderId}")
    public AjaxResult getOrderDetail(@PathVariable String orderId){
        OrderVO orderVO = orderService.queryOrderDetail(orderId);
        return AjaxResult.success("order",orderVO);
    }


}
