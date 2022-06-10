package com.gangu.seckill.controller;

import com.gangu.seckill.pojo.User;
import com.gangu.seckill.service.OrderService;
import com.gangu.seckill.vo.OrderDetailVo;
import com.gangu.seckill.vo.RespBean;
import com.gangu.seckill.vo.RespBeanEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @GetMapping("/detail")
    @ResponseBody
    public RespBean detail(User user, Long orderId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        OrderDetailVo detail = orderService.orderDetail(orderId);
        return RespBean.success(detail);
    }


}
