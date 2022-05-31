package com.gangu.seckill.mapper;

import com.gangu.seckill.pojo.Order;
import org.apache.ibatis.annotations.Insert;

public interface OrderMapper {

    void insert(Order order);
}
