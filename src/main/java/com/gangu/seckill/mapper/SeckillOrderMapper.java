package com.gangu.seckill.mapper;


import com.gangu.seckill.pojo.SeckillOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SeckillOrderMapper {

    @Select("select * from t_seckill_order where user_id = #{userId} ;")
    SeckillOrder getSeckillOrder(@Param("userId") Long userId);


    void save(SeckillOrder seckillOrder);
}
