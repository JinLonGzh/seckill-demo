package com.gangu.seckill.controller;

import com.gangu.seckill.pojo.Order;
import com.gangu.seckill.pojo.SeckillOrder;
import com.gangu.seckill.pojo.User;
import com.gangu.seckill.service.GoodsService;

import com.gangu.seckill.service.SeckillOrderService;
import com.gangu.seckill.vo.GoodsVo;
import com.gangu.seckill.vo.RespBeanEnum;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Resource
    private GoodsService goodsService;
    @Resource
    private SeckillOrderService seckillOrderService;

    @RequestMapping("/doSeckill")
    public String doSecKill(Model model, User user, Long goodsId) {
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

}
