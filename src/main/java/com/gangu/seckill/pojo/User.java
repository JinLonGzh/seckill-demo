package com.gangu.seckill.pojo;


import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * @since 2022-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class User {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID shoujihaoma
     */
    private Long id;

    private String nickname;

    /**
     * MD5二次加密
     */
    private String password;

    private String slat;

    /**
     * 头像
     */
    private String head;

    /**
     * 注册时间
     */
    private LocalDateTime registerDate;

    /**
     * 最后一次登录时间
     */
    private LocalDateTime lastLoginDate;

    /**
     * 登录次数
     */
    private Integer loginCount;



}
