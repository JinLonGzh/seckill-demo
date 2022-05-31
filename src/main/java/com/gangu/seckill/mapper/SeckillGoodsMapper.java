package com.gangu.seckill.mapper;


import com.gangu.seckill.pojo.SeckillGoods;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


public interface SeckillGoodsMapper {


    @Select("select * from t_seckill_goods where goods_id = #{goodsId};")
    SeckillGoods getSeckillGoods(@Param("goodsId") Long goodsId);


    void updateSeckillGoods(SeckillGoods seckillGoods);
}
