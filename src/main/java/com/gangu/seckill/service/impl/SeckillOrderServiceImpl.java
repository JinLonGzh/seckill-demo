package com.gangu.seckill.service.impl;
import java.math.BigDecimal;
import java.util.Date;

import com.gangu.seckill.mapper.OrderMapper;
import com.gangu.seckill.mapper.SeckillGoodsMapper;
import com.gangu.seckill.mapper.SeckillOrderMapper;
import com.gangu.seckill.pojo.Order;
import com.gangu.seckill.pojo.SeckillGoods;
import com.gangu.seckill.pojo.SeckillOrder;
import com.gangu.seckill.pojo.User;
import com.gangu.seckill.service.GoodsService;
import com.gangu.seckill.service.SeckillGoodsService;
import com.gangu.seckill.service.SeckillOrderService;
import com.gangu.seckill.vo.GoodsVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Resource
    private SeckillOrderMapper seckillOrderMapper;
    @Resource
    private SeckillGoodsMapper seckillGoodsMapper;
    @Resource
    private OrderMapper orderMapper;

    //根据用户Id获取已经秒杀的订单
    public SeckillOrder getSeckillOrder(Long userId) {
        SeckillOrder seckillOrder = seckillOrderMapper.getSeckillOrder(userId);
        return seckillOrder;
    }

    //秒杀操作
    @Override
    public Order seckill(User user, GoodsVo goodsVo) {
        //秒杀商品减库存
        SeckillGoods seckillGoods = seckillGoodsMapper.getSeckillGoods(goodsVo.getId());
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
        seckillGoodsMapper.updateSeckillGoods(seckillGoods);
        //生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goodsVo.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);

//        //生成秒杀订单
//        SeckillOrder seckillOrder = new SeckillOrder();
//        seckillOrder.setUserId(user.getId());
//        seckillOrder.setOrderId(order.getId());
//        seckillOrder.setGoodsId(goodsVo.getId());
//        seckillOrderMapper.save(seckillOrder);

        return order;
    }

}
