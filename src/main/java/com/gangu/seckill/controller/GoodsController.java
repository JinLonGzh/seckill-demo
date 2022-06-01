package com.gangu.seckill.controller;

import com.gangu.seckill.pojo.User;
import com.gangu.seckill.service.GoodsService;
import com.gangu.seckill.service.UserService;
import com.gangu.seckill.vo.GoodsVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Resource
    private UserService userService;

    @Resource
    private GoodsService goodsService;

    @RequestMapping("/toList")
    public String toList(Model model, User user) {
        model.addAttribute("user", user);
        List<GoodsVo> goodsVos = goodsService.FindGoodsVo();
        model.addAttribute("goodsList", goodsVos);
        return "goodsList";
    }

    @RequestMapping("/toDetail/{goodsId}")
    public String toDetail(Model model, User user, @PathVariable("goodsId") Long id) {
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodVoById(id);

        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();

        //秒杀状态
        int seckillStatus = 0;//0还未开始，1正在秒杀，2秒杀结束
        //秒杀倒计时
        int remainSeconds = 0;

        if (nowDate.before(startDate)) {
            remainSeconds = ((int) (startDate.getTime() - nowDate.getTime()) / 1000);
        } else if (nowDate.after(endDate)) {
            //已结束
            seckillStatus = 2;
            remainSeconds = -1;
        } else {
            seckillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("secKillStatus", seckillStatus);
        model.addAttribute("goods", goodsVo);
        return "goodsDetail";
    }


}
