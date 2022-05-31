package com.gangu.seckill.controller;

import com.gangu.seckill.service.UserService;
import com.gangu.seckill.vo.LoginVo;
import com.gangu.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Resource
    private UserService UserService;

    @RequestMapping("/tologin")
    public String toLogin(){
        return "login";
    }

    @PostMapping("/dologin")
    @ResponseBody
    public RespBean doLogin(@Valid LoginVo loginVo, HttpServletRequest request , HttpServletResponse response){
        return UserService.doLogin(loginVo,request,response);

    }

}
