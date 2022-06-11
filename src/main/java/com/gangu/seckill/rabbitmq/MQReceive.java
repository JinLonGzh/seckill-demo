package com.gangu.seckill.rabbitmq;

import com.gangu.seckill.pojo.SeckillMessage;
import com.gangu.seckill.pojo.SeckillOrder;
import com.gangu.seckill.pojo.User;
import com.gangu.seckill.service.GoodsService;
import com.gangu.seckill.service.OrderService;
import com.gangu.seckill.service.SeckillOrderService;
import com.gangu.seckill.utils.JsonUtil;
import com.gangu.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
@Slf4j
public class MQReceive {

    @Resource
    private GoodsService goodsService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private SeckillOrderService seckillOrderService;

    @RabbitListener(queues = "seckillQueue")
    public void receive(String msg) {
        log.info("接收到的消息:{}", msg);
        SeckillMessage message = JsonUtil.jsonStr2Object(msg, SeckillMessage.class);

        Long goodId = message.getGoodId();
        User user = message.getUser();
        GoodsVo goodsVo = goodsService.findGoodVoById(goodId);
        //判断库存
        if (goodsVo.getStockCount() < 1) {
            return;
        }
        String seckillOrderJson = (String)
                redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodId);
        if (!StringUtils.isEmpty(seckillOrderJson)) {
            return;
        }
        seckillOrderService.seckill(user,goodsVo);
    }

}
