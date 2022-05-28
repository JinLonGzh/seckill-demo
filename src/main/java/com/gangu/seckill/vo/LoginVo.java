package com.gangu.seckill.vo;


import com.gangu.seckill.validator.IsMobile;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;


/**
 * 登录参数
 */
@Data
@ToString
public class LoginVo {

	@NotNull
	@IsMobile
	private String mobile;

	@NotNull
	@Length(min = 32)
	private String password;

}