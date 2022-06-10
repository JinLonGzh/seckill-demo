package com.gangu.seckill.service;


import com.gangu.seckill.vo.OrderDetailVo;

public interface OrderService {


    OrderDetailVo orderDetail(Long orderId);
}
