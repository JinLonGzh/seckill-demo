package com.gangu.seckill.controller;

import com.gangu.seckill.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @RequestMapping("/toList")
    public String toList(HttpSession httpSession, Model model, @CookieValue("userTicket") String ticker) {
        if (StringUtils.isEmpty(ticker)){
            return "login";
        }
        User user = (User) httpSession.getAttribute(ticker);
        if (user == null){
            return "login";
        }
        model.addAttribute("user",user);
        return "goodsList";
    }


}
