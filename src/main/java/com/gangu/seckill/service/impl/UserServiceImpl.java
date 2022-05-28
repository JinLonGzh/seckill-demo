package com.gangu.seckill.service.impl;

import com.gangu.seckill.exception.GlobalException;
import com.gangu.seckill.pojo.User;
import com.gangu.seckill.mapper.UserMapper;
import com.gangu.seckill.service.UserService;
import com.gangu.seckill.utils.CookieUtil;
import com.gangu.seckill.utils.MD5Util;
import com.gangu.seckill.utils.UUIDUtil;
import com.gangu.seckill.vo.LoginVo;
import com.gangu.seckill.vo.RespBean;
import com.gangu.seckill.vo.RespBeanEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author
 * @since 2022-05-27
 */
@Service
public class UserServiceImpl implements UserService{

    @Resource
    private UserMapper userMapper;

    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
//        if (mobile == null) {
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
//        }
//
//        if (!ValidatorUtil.isMobile(mobile)) {
//            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
//        }

        //根据手机号获取用户
        User user = userMapper.selectById(mobile);
        if (user == null) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        //判断密码
        if (!MD5Util.formPassToDBPass(password, user.getSlat()).equals(user.getPassword())) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        //生成cookie
        String ticket = UUIDUtil.uuid();
        request.getSession().setAttribute(ticket,user);
        CookieUtil.setCookie(request,response,"userTicket",ticket);


        return RespBean.success();
    }
}