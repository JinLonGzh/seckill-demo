package com.gangu.seckill.service;

import com.gangu.seckill.pojo.Order;
import com.gangu.seckill.pojo.SeckillOrder;
import com.gangu.seckill.pojo.User;
import com.gangu.seckill.vo.GoodsVo;

public interface SeckillOrderService {
    //根据用户Id获取已经秒杀的订单
    SeckillOrder getSeckillOrder(Long userId);

    //秒杀操作
    Order seckill(User user, GoodsVo goodsVo);

    /**
     * 获取秒杀结果
     *
     * @return orderId：成功，-1 失败，0 排队中
     * @Param
     */
    Long getResult(User user, Long goodsId);
}
