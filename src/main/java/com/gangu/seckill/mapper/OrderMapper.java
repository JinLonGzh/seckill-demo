package com.gangu.seckill.mapper;

import com.gangu.seckill.pojo.Order;
import com.gangu.seckill.vo.OrderDetailVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface OrderMapper {

    void insert(Order order);

    Order selectOrderById(@Param("orderId") Long orderId);
}
