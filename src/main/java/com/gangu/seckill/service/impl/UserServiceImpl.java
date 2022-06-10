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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate redisTemplate;

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
//        request.getSession().setAttribute(ticket,user);
        redisTemplate.opsForValue().set("user:" + ticket, user);
        CookieUtil.setCookie(request, response, "userTicket", ticket);


        return RespBean.success();
    }

    /**
     * 根据Cookie获取用户
     */
    @Override

    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if (userTicket.isEmpty()) {
            return null;
        }

        User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);

        if (user != null) {
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }
        return user;
    }

    /**
     * 更改密码
     * @Param
     * @return
     */
    @Override
    public RespBean updatePassword(String userTicket, String password,
                                   HttpServletRequest request, HttpServletResponse response) {
        User user = getUserByCookie(userTicket, request, response);
        if (user == null) {
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }
        user.setPassword(MD5Util.inputPassToDBPass(password, user.getSlat()));
        int result = userMapper.updateById(user);
        if (1 == result) {
            //删除Redis
            redisTemplate.delete("user:" + userTicket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
    }

}
