package com.gangu.seckill.service.impl;

import com.gangu.seckill.mapper.OrderMapper;
import com.gangu.seckill.pojo.Goods;
import com.gangu.seckill.pojo.Order;
import com.gangu.seckill.pojo.User;
import com.gangu.seckill.service.OrderService;
import com.gangu.seckill.service.SeckillGoodsService;
import com.gangu.seckill.vo.GoodsVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private SeckillGoodsService seckillGoodsService;


}

