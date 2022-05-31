package com.gangu.seckill.service;


import com.gangu.seckill.pojo.SeckillGoods;

public interface SeckillGoodsService {


    SeckillGoods getSeckillGoods(Long goodsId);

    //更新数据，更新t_seckill_goods
    void updateSeckillGoods(SeckillGoods seckillGoods);
}

