package com.gangu.seckill.service.impl;

import com.gangu.seckill.mapper.GoodsMapper;
import com.gangu.seckill.pojo.Goods;
import com.gangu.seckill.service.GoodsService;
import com.gangu.seckill.vo.GoodsVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GoodServiceImpl implements GoodsService {

    @Resource
    private GoodsMapper goodsMapper;

    @Override
    public List<GoodsVo> FindGoodsVo() {
        List<GoodsVo> goodsVos = goodsMapper.findGoodsVo();
        return goodsVos;
    }

    @Override
    public GoodsVo findGoodVoById(Long goodsId) {
        GoodsVo goodsVo= goodsMapper.findGoodVoById(goodsId);
        return goodsVo;
    }

}

