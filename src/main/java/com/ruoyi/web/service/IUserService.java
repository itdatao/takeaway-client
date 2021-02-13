package com.ruoyi.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.web.domain.User;
import com.ruoyi.web.vo.RegisterVO;
import com.ruoyi.web.vo.UserVO;

import java.util.List;

/**
 * 用户Service接口
 *
 * @author ruoyi
 * @date 2021-01-13
 */
public interface IUserService extends IService<User> {

    String checkUserNameUnique(String nickname);

    String checkPhoneUnique(User user);

    int resetPwd(User user);

    int register(RegisterVO user);

    int resetUserPwd(String userName, String password);

    String login(String phone, String password);

    UserVO getUserInfoFromToken(String token);

    int updateUserInfo(String token,UserVO userVO);

    void updateAvatar(String token, String fileURL);

    int updatePassword(String oldPassword, String newPassword, String token);

    User getByOpenId(String openid);

    int bindPhone(String phone, String token);
}