package com.gangu.seckill.service;


import com.gangu.seckill.vo.LoginVo;
import com.gangu.seckill.vo.RespBean;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2022-05-27
 */
public interface UserService{

    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);
}
