package com.gangu.seckill.config;


import com.gangu.seckill.pojo.User;
import com.gangu.seckill.service.UserService;
import com.gangu.seckill.utils.CookieUtil;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 该类用于WebConfig配置类的addArgumentResolvers方法传入
 */
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Resource
    private UserService userService;

    /**
     * 该方法用于判断Controller中方法参数中是否有符合条件的参数：
     * 有则进入下一个方法resolveArgument；
     * 没有则跳过不做处理
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 获取传入参数的类型
        Class<?> clazz = parameter.getParameterType();
        return clazz == User.class;
    }

    /**
     * 在这里可以进行处理，根据情况返回对象——返回的对象将被赋值到Controller的方法的参数中
     */
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        String ticket = CookieUtil.getCookieValue(request, "userTicket");
        if (ticket == null) {
            return null;
        }
        return userService.getUserByCookie(ticket, request, response);


    }
}
