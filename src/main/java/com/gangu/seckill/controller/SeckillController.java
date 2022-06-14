package com.gangu.seckill.controller;

import com.gangu.seckill.exception.GlobalException;
import com.gangu.seckill.pojo.Order;
import com.gangu.seckill.pojo.SeckillMessage;
import com.gangu.seckill.pojo.SeckillOrder;
import com.gangu.seckill.pojo.User;
import com.gangu.seckill.rabbitmq.MQSender;
import com.gangu.seckill.service.GoodsService;

import com.gangu.seckill.service.SeckillOrderService;
import com.gangu.seckill.utils.JsonUtil;
import com.gangu.seckill.vo.GoodsVo;
import com.gangu.seckill.vo.RespBean;
import com.gangu.seckill.vo.RespBeanEnum;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/seckill")
@Slf4j
public class SeckillController implements InitializingBean {

    @Resource
    private GoodsService goodsService;
    @Resource
    private SeckillOrderService seckillOrderService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private MQSender mqSender;
    @Resource
    private RedisScript<Boolean> script;

    //内存标记
    private Map<Long, Boolean> EmptyStockMap = new HashMap<>();


    /**
     * 秒杀操作
     *
     * @return
     * @Param
     */
    @PostMapping("/doSeckill")
    @ResponseBody
    public RespBean doSecKill(User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        ValueOperations valueOperations = redisTemplate.opsForValue();
        //判断是否重复秒杀
        String seckillOrderJson = (String) redisTemplate.opsForValue().get(("order:" + user.getId() + ":" + goodsId));
        if (seckillOrderJson != null) {
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        //内存标记：提前判断库存是否为0, 减少访问redis的次数
        if (EmptyStockMap.get(goodsId)) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //预减库存
        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
//        Long stock = (Long) redisTemplate.execute(script,
//                Collections.singletonList("seckillGoods:" + goodsId), Collections.EMPTY_LIST);
        if (stock < 0) {
            EmptyStockMap.put(goodsId, true);   //
            valueOperations.increment("seckillGoods:" + goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        //向rabbitmq发送秒杀信息
        mqSender.sendsecKillMessage(JsonUtil.object2JsonStr(seckillMessage));

        return RespBean.success(0);

    }

    /**
     * 获取秒杀结果
     *
     * @return orderId：成功，-1 失败，0 排队中
     * @Param
     */
    @GetMapping("/result")
    @ResponseBody
    public RespBean getResult(User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        Long orderId = seckillOrderService.getResult(user, goodsId);
        return RespBean.success(orderId);
    }

    /**
     * 获取验证码
     * @Param
     * @return
     */
    @GetMapping("/captcha")
    public void captcha(User user,HttpServletResponse response) throws Exception {
        if (null == user) {
            throw new GlobalException(RespBeanEnum.REQUEST_ILLEGAL);
        }
        // 设置请求头为输出图片类型
        response.setContentType("image/jpg");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //生成验证码，将结果放入redis
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 32, 3);

        redisTemplate.opsForValue().set("captcha:" + user.getId(), captcha.text
                (), 300, TimeUnit.SECONDS);
        try {
            // 输出图片流
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.error("验证码生成失败", e.getMessage());
        }
    }


    /**
     * 系统初始化，把商品库存数量加载到Redis
     *
     * @return
     * @Param
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.FindGoodsVo();
        if (CollectionUtils.isEmpty(goodsVoList)) {
            return;
        }
        goodsVoList.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(), goodsVo.getStockCount());
            //false就是还有
            EmptyStockMap.put(goodsVo.getId(), false);
        });

    }
}
