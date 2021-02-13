package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.Order;
import org.apache.ibatis.annotations.Param;

/**
 * 订单Mapper接口
 *
 * @author hht
 * @date 2021-01-30
 */
public interface OrderMapper extends BaseMapper<Order> {

    int updateOrderStatus(@Param("orderId") String orderId, @Param("orderStatus") Integer orderStatus);
}
