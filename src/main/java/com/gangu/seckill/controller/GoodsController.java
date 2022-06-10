package com.gangu.seckill.controller;

import com.gangu.seckill.pojo.User;
import com.gangu.seckill.service.GoodsService;
import com.gangu.seckill.service.UserService;
import com.gangu.seckill.vo.DetailVo;
import com.gangu.seckill.vo.GoodsVo;
import com.gangu.seckill.vo.RespBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Resource
    private UserService userService;

    @Resource
    private GoodsService goodsService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private ThymeleafViewResolver thymeleafViewResolver;


    @RequestMapping(value = "/toList", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, User user,
                         HttpServletRequest request, HttpServletResponse response) {
        //Redis中获取页面，如果不为空，直接返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.FindGoodsVo());
        // return "goodsList";
        //如果为空，手动渲染，存入Redis并返回
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(),
                model.asMap());
        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", context);
        if (!StringUtils.isEmpty(html)) {
            valueOperations.set("goodsList", html, 60, TimeUnit.SECONDS);
        }
        return html;
    }

    /**
     * 展示商品详情页
     *
     * @return
     * @Param
     */
    @RequestMapping(value = "/toDetail2/{goodsId}", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail2(Model model, User user, @PathVariable("goodsId") Long id,
                            HttpServletRequest request, HttpServletResponse response) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //Redis中获取页面，如果不为空，直接返回页面
        String html = (String) valueOperations.get("goodsDetail:" + id);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

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
//        return "goodsDetail";
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(),
                model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", context);
        if (!StringUtils.isEmpty(html)) {
            valueOperations.set("goodsDetail:" + id, html, 60, TimeUnit.SECONDS);
        }
        return html;

    }

    /**
     * 展示商品详情页
     *
     * @return
     * @Param
     */
    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public RespBean toDetail(Model model, User user, @PathVariable("goodsId") Long id) {
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
        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setSecKillStatus(seckillStatus);
        detailVo.setRemainSeconds(remainSeconds);

        return RespBean.success(detailVo);

    }


}
