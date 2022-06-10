package com.gangu.seckill.mapper;

import com.gangu.seckill.pojo.User;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2022-05-27
 */
public interface UserMapper{

    User selectById(@Param("id") String mobile);

    /**
     * 更改密码
     * @Param
     * @return
     */
    int updateById(User user);
}
