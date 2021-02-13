package com.ruoyi.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author Huhuitao
 * @Date 2021/1/30 21:53
 */
public interface OrderItemMapper extends BaseMapper<OrderItem> {

    int deleteByOrderIds(@Param("orderIds") List<String> orderIds);
}
