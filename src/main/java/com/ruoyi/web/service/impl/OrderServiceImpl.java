package com.ruoyi.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.ruoyi.web.common.Constants;
import com.ruoyi.web.domain.*;
import com.ruoyi.web.exception.CustomException;
import com.ruoyi.web.form.CartForm;
import com.ruoyi.web.form.OrderForm;
import com.ruoyi.web.mapper.*;
import com.ruoyi.web.service.IOrderService;
import com.ruoyi.web.utils.JwtUtil;
import com.ruoyi.web.vo.OrderItemVO;
import com.ruoyi.web.vo.OrderVO;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单Service业务层处理
 *
 * @author hht
 * @date 2021-01-30
 */
@Service("OrderServiceImpl")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private FoodMapper foodMapper;
    @Autowired
    private TUserMapper userMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private ShopMapper shopMapper;


    /**
     * 取消订单
     *
     * @param orderId
     * @return
     */
    @Override
    public int cancelOrder(String orderId) {
        // 判断订单是否已支付，如果已支付需要退款
        Order order = orderMapper.selectById(orderId);
        if (order != null && order.getPayStatus() == Constants.PAY_STATUS_PAID) {
            //todo 支付宝退款 设置已支付
            order.setPayStatus(Constants.PAY_STATUS_PAID);
        }else{
            // 未支付状态
            order.setPayStatus(Constants.PAY_STATUS_UNPAID);
        }
        order.setOrderStatus(Constants.ORDER_STATUS_CANCELED);
        // 修改订单状态
        return orderMapper.updateById(order);
    }

    //查询订单详细信息包括订单详情
    @Override
    public OrderVO queryOrderDetail(String orderId) {
        if (StringUtils.isBlank(orderId)) {
            throw new CustomException("订单id为空");
        }
        OrderVO orderVO = new OrderVO();
        //查询订单信息
        Order order = orderMapper.selectById(orderId);
        //查询订单详情列表
        QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        List<OrderItem> orderItems = orderItemMapper.selectList(queryWrapper);
        List<OrderItemVO> orderItemVOList = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(orderItem,orderItemVO);
            orderItemVO.setAmount(orderItem.getNumber());
            orderItemVO.setFoodUrl(orderItem.getFoodCover());
            orderItemVO.setProductId(orderItem.getDetailId());
            orderItemVOList.add(orderItemVO);
        }
        String sellId = order.getSellId();
        Shop shop = shopMapper.selectById(sellId);
        //封装数据
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setOrderItems(orderItemVOList);
        orderVO.setShop(shop);
        return orderVO;
    }

    @Transactional
    @Override
    public String placeOrder(OrderForm orderForm) {
        if (orderForm == null) throw new CustomException("下单失败，订单信息不完整");

        String remark = orderForm.getRemark();
        String shopId = orderForm.getShopId();
        String userId = orderForm.getUserId();
        List<CartForm> productList = orderForm.getProductList();
        // 查询用户信息
        User user = userMapper.selectById(userId);
        // 查询商家信息
        Shop shop = shopMapper.selectById(shopId);
        // 创建订单对象
        Order order = new Order();
        // 订单项集合
        List<OrderItem> orderItems = new ArrayList<>();
        // 查询餐品  计算总价格
        BigDecimal foodsPrice = new BigDecimal(0);
        for (CartForm cartForm : productList) {
            // 创建订单详情对象
            OrderItem orderItem = new OrderItem();
            Food food = foodMapper.selectById(cartForm.getProductId());
            // 修改下单餐品的销量
            food.setSale(cartForm.getQuantity()+food.getSale());
            foodMapper.updateById(food);

            // 封装订单详情
            orderItem.setFoodCover(food.getCover());
            orderItem.setFoodName(food.getName());
            orderItem.setFoodPrice(food.getPrice());
            orderItem.setNickname(user.getNickname());
            orderItem.setNumber(cartForm.getQuantity());
            orderItem.setTotal(food.getPrice().multiply(new BigDecimal(cartForm.getQuantity())));
            orderItems.add(orderItem);
            foodsPrice = food.getPrice().multiply(new BigDecimal(cartForm.getQuantity())).add(foodsPrice);
        }
        // 封装订单对象
        order.setAddress(user.getCity());
        order.setFoodsPrice(foodsPrice);
        order.setFreightPrice(shop.getDeliveryFee());
        order.setNickname(user.getNickname());
        order.setPhone(user.getPhone());
        order.setRemark(remark);
        order.setSellId(shop.getSellId());
        order.setUserId(user.getId());
        order.setPayStatus(Constants.PAY_STATUS_UNPAID);
        order.setOrderStatus(Constants.ORDER_STATUS_NO_CHECK);
        order.setPayMethod("0");
        order.setOrderPrice(foodsPrice.add(shop.getDeliveryFee()));
        // 插入订单对象
        orderMapper.insert(order);
        // 插入订单详情表
        orderItems.forEach((item)->{
            item.setOrderId(order.getOrderId());
            orderItemMapper.insert(item);
        });

        // 获取订单编号
        return order.getOrderId();
    }

    @Override
    public List<OrderVO> getOrderList(String token,Integer page,Integer size) throws ExpiredJwtException {
        Page<Order> pageInfo = new Page<>();
        String userId = JwtUtil.getSubject(token, "id");
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        queryWrapper.orderByDesc("create_time");
        List<Order> orders = orderMapper.selectPage(pageInfo,queryWrapper).getRecords();

        List<OrderVO> orderVOS = new ArrayList<>();
        orders.forEach((o)->{
            QueryWrapper<OrderItem> itemQueryWrapper = new QueryWrapper<>();
            itemQueryWrapper.eq("order_id", o.getOrderId());
            List<OrderItem> orderItems = orderItemMapper.selectList(itemQueryWrapper);
            List<OrderItemVO> orderItemVOList = new ArrayList<>();

            for (OrderItem orderItem : orderItems) {
                OrderItemVO orderItemVO = new OrderItemVO();
                BeanUtils.copyProperties(orderItem,orderItemVO);
                orderItemVO.setAmount(orderItem.getNumber());
                orderItemVO.setFoodUrl(orderItem.getFoodCover());
                orderItemVO.setProductId(orderItem.getDetailId());
                orderItemVOList.add(orderItemVO);
            }
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(o,orderVO);
            String sellId = o.getSellId();
            Shop shop = shopMapper.selectById(sellId);
            orderVO.setShop(shop);
            orderVO.setOrderItems(orderItemVOList);
            orderVOS.add(orderVO);
        });
        return orderVOS;
    }


    @Override
    public List<Order> queryList(Order order) {
        LambdaQueryWrapper<Order> lqw = Wrappers.lambdaQuery();

        if (order.getOrderStatus() != null) {
            lqw.eq(Order::getOrderStatus, order.getOrderStatus());
        }
        if (StringUtils.isNotBlank(order.getNickname())) {
            lqw.like(Order::getNickname, order.getNickname());
        }
        if (StringUtils.isNotBlank(order.getPayMethod())) {
            lqw.eq(Order::getPayMethod, order.getPayMethod());
        }
        if (order.getPayStatus() != null) {
            lqw.eq(Order::getPayStatus, order.getPayStatus());
        }
        if (order.getOrderNo() != null) {
            lqw.eq(Order::getOrderNo, order.getOrderNo());
        }
        return this.list(lqw);
    }
}
