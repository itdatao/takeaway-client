package com.ruoyi.web.mapper;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.web.domain.User;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Mapper接口
 *
 * @author ruoyi
 * @date 2021-01-13
 */
public interface TUserMapper extends BaseMapper<User> {


    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    public int checkUserNameUnique(@Param("userName") String userName);

    /**
     * 校验手机号码是否唯一
     *
     * @param phoneNumber 手机号码
     * @return 结果
     */
    public User checkPhoneUnique(@Param("phoneNumber") String phoneNumber);

    /*修改密码*/
    public int resetUserPwd(@Param("password") String userPwd, @Param("userId") String userId);

    void updateLoginTime(@Param("id") String id,@Param("dateTime") DateTime date);
}