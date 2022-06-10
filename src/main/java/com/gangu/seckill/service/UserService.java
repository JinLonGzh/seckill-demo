package com.gangu.seckill.service;


import com.gangu.seckill.pojo.User;
import com.gangu.seckill.vo.LoginVo;
import com.gangu.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author
 * @since 2022-05-27
 */
public interface UserService {

    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    /**
     * 根据Cookie获取用户
     */
    User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response);

    /**
     * 更改密码
     * @Param
     * @return
     */
    RespBean updatePassword(String userTicket, String password, HttpServletRequest request,
                            HttpServletResponse response);
}
