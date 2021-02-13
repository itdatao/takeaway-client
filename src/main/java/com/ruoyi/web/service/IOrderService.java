package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.ruoyi.web.domain.Order;
import com.ruoyi.web.form.OrderForm;
import com.ruoyi.web.vo.OrderVO;

import java.util.List;

/**
 * 订单Service接口
 *
 * @author hht
 * @date 2021-01-30
 */
public interface IOrderService extends IService<Order> {

    /**
     * 查询列表
     */
    List<Order> queryList(Order order);
    /*取消订单*/
    int cancelOrder(String orderId);

    OrderVO queryOrderDetail(String orderId);

    String placeOrder(OrderForm orderForm);

    List<OrderVO> getOrderList(String token,Integer page,Integer size);
}
