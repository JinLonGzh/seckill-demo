package com.gangu.seckill.mapper;


import com.gangu.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsMapper {
    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodVoById(@Param("goodsId") Long goodsId);
}
