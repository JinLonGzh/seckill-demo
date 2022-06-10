package com.gangu.seckill.service.impl;

import com.gangu.seckill.exception.GlobalException;
import com.gangu.seckill.mapper.OrderMapper;
import com.gangu.seckill.pojo.Goods;
import com.gangu.seckill.pojo.Order;
import com.gangu.seckill.pojo.User;
import com.gangu.seckill.service.GoodsService;
import com.gangu.seckill.service.OrderService;
import com.gangu.seckill.service.SeckillGoodsService;
import com.gangu.seckill.vo.GoodsVo;
import com.gangu.seckill.vo.OrderDetailVo;
import com.gangu.seckill.vo.RespBeanEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private GoodsService goodsService;

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private SeckillGoodsService seckillGoodsService;


    @Override
    public OrderDetailVo orderDetail(Long orderId) {
        if (null == orderId) {
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        Order order = orderMapper.selectOrderById(orderId);
        GoodsVo goodVo = goodsService.findGoodVoById(order.getGoodsId());
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrder(order);
        orderDetailVo.setGoodsVo(goodVo);

        return orderDetailVo;
    }
}

