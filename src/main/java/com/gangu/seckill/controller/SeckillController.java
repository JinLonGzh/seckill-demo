package com.gangu.seckill.controller;

import com.gangu.seckill.pojo.Order;
import com.gangu.seckill.pojo.SeckillOrder;
import com.gangu.seckill.pojo.User;
import com.gangu.seckill.service.GoodsService;

import com.gangu.seckill.service.SeckillOrderService;
import com.gangu.seckill.vo.GoodsVo;
import com.gangu.seckill.vo.RespBean;
import com.gangu.seckill.vo.RespBeanEnum;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Resource
    private GoodsService goodsService;
    @Resource
    private SeckillOrderService seckillOrderService;

    /**
     * 秒杀操作
     * @Param
     * @return
     */
    @RequestMapping("/doSeckill2")
    public String doSecKill2(Model model, User user, Long goodsId) {
        if (user == null) {
            return "login";
        }
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodVoById(goodsId);
        Integer stockCount = goodsVo.getStockCount(); //库存数量
        //判断库存
        if (stockCount < 1) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "secKillFail";
        }
        //判断是否重复秒杀
        SeckillOrder seckillOrder = seckillOrderService.getSeckillOrder(user.getId());
        if (seckillOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
            return "secKillFail";
        }

        Order order = seckillOrderService.seckill(user, goodsVo);

        model.addAttribute("order", order);
        model.addAttribute("goods", goodsVo);
        return "orderDetail";
    }


    /**
     * 秒杀操作
     * @Param
     * @return
     */
    @PostMapping("/doSeckill")
    @ResponseBody
    public RespBean doSecKill(Model model, User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodVoById(goodsId);
        Integer stockCount = goodsVo.getStockCount(); //库存数量
        //判断库存
        if (stockCount < 1) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //判断是否重复秒杀
        SeckillOrder seckillOrder = seckillOrderService.getSeckillOrder(user.getId());
        if (seckillOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }

        Order order = seckillOrderService.seckill(user, goodsVo);


        return RespBean.success(order);
    }

}
