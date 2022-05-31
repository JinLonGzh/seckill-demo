package com.gangu.seckill.service;


import com.gangu.seckill.vo.GoodsVo;
import java.util.List;

public interface GoodsService {

    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> FindGoodsVo();

    GoodsVo findGoodVoById(Long goodsId);
}
