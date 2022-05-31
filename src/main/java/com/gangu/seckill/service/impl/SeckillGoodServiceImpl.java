package com.gangu.seckill.service.impl;


import com.gangu.seckill.mapper.SeckillGoodsMapper;
import com.gangu.seckill.pojo.SeckillGoods;
import com.gangu.seckill.service.SeckillGoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class SeckillGoodServiceImpl implements SeckillGoodsService {

    @Resource
    private SeckillGoodsMapper seckillGoodsMapper;


    //获取秒杀商品实体类 SeckillGoods
    @Override
    public SeckillGoods getSeckillGoods(Long goodsId) {

        return seckillGoodsMapper.getSeckillGoods(goodsId);

    }

    //更新数据，更新t_seckill_goods
    @Override
    public void updateSeckillGoods(SeckillGoods seckillGoods) {
        seckillGoodsMapper.updateSeckillGoods(seckillGoods);
    }
}
