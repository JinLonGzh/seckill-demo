package com.gangu.seckill.vo;

import com.gangu.seckill.utils.ValidatorUtil;
import com.gangu.seckill.validator.IsMobile;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * ConstraintValidator<PersonName, String>：
 * 泛型一：校验的注解类
 * 泛型二：被校验的对象类型
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {

	private boolean required = false;

	/**
	 * @param constraintAnnotation 待校验注解的所有信息
	 */
	@Override
	public void initialize(IsMobile constraintAnnotation) {
		//取出注解中的数据
		required = constraintAnnotation.required();
	}

	/**
	 * 在这里面定义校验规则
	 * @param value 待校验对象
	 * @param context
	 * @return true 通过校验 false 无法通过校验
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (required){
			return ValidatorUtil.isMobile(value);
		}else {
			if (StringUtils.isEmpty(value)){
				return true;
			}else {
				return ValidatorUtil.isMobile(value);
			}
		}
	}
}