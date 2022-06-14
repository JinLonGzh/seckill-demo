package com.gangu.seckill.service.impl;

import com.gangu.seckill.mapper.GoodsMapper;
import com.gangu.seckill.pojo.Goods;
import com.gangu.seckill.pojo.User;
import com.gangu.seckill.service.GoodsService;
import com.gangu.seckill.vo.GoodsVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GoodServiceImpl implements GoodsService {

    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public List<GoodsVo> FindGoodsVo() {
        List<GoodsVo> goodsVos = goodsMapper.findGoodsVo();
        return goodsVos;
    }

    @Override
    public GoodsVo findGoodVoById(Long goodsId) {
        GoodsVo goodsVo = goodsMapper.findGoodVoById(goodsId);
        return goodsVo;
    }

    /**
     * 判断验证码
     *
     * @return
     * @Param
     */
    @Override
    public boolean checkCaptcha(User user, Long goodsId, String captcha) {
        if (StringUtils.isEmpty(captcha) || null == user || goodsId < 0) {
            return false;
        }
        String redisCaptcha = (String) redisTemplate.opsForValue().get("captcha:" +
                user.getId());
        return redisCaptcha.equals(captcha);

    }

}

