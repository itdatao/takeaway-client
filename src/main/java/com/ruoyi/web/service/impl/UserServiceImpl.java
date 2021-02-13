package com.ruoyi.web.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.common.Constants;
import com.ruoyi.web.common.UserConstants;
import com.ruoyi.web.domain.User;
import com.ruoyi.web.exception.CustomException;
import com.ruoyi.web.mapper.TUserMapper;
import com.ruoyi.web.service.IUserService;
import com.ruoyi.web.utils.JwtUtil;
import com.ruoyi.web.utils.SecurityUtils;
import com.ruoyi.web.utils.StringUtils;
import com.ruoyi.web.vo.RegisterVO;
import com.ruoyi.web.vo.UserVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * 用户Service业务层处理
 *
 * @author ruoyi
 * @date 2021-01-13
 */
@Service
@Primary
@Slf4j
public class UserServiceImpl extends ServiceImpl<TUserMapper, User> implements IUserService {

    @Autowired
    private TUserMapper userMapper;


    /**
     * 校验用户名是否唯一
     *
     * @param nickname
     * @return
     */
    @Override
    public String checkUserNameUnique(String nickname) {
        int count = userMapper.checkUserNameUnique(nickname);
        if (count > 0) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;

    }

    @Override
    public User getByOpenId(String openid) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("openid", openid);
        return baseMapper.selectOne(userQueryWrapper);
    }

    @Override
    public int updatePassword(String oldPassword, String newPassword, String token) throws ExpiredJwtException, CustomException {
        // 根据token 旧密码 新密码修改用户密码 先判断旧密码是否正确，再修改密码

        //获取用户id
        Claims claims = JwtUtil.checkJWT(token);
        String id = (String) claims.get("id");
        log.info("userId: {}", id);
        User user = baseMapper.selectById(id);
        String password = user.getPassword();
        //判断用户旧密码是否正确
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            throw new CustomException("修改密码失败，旧密码错误");
        }

        //跟新新密码
        newPassword = SecurityUtils.encryptPassword(newPassword);
        user.setPassword(newPassword);
        return baseMapper.updateById(user);


    }

    @Override
    public int bindPhone(String phone, String token) throws ExpiredJwtException{
        Claims claims = JwtUtil.checkJWT(token);
        String id = (String) claims.get("id");
        log.info("userId: {}", id);
        // 校验手机号 是否已绑定
        if (StringUtils.isNoneBlank(phone)) {
            User user = userMapper.checkPhoneUnique(phone);
            if (user != null) {
                throw new CustomException("该手机号已被绑定,请换一个吧");
            }
        }
        //设置手机号
        User user = new User();
        user.setId(id);
        user.setPhone(phone);
        return userMapper.updateById(user);
    }

    @Override
    public void updateAvatar(String token, String fileURL) throws ExpiredJwtException {

        Claims claims = JwtUtil.checkJWT(token);
        String id = (String) claims.get("id");
        log.info("userId: {}", id);
        User user = new User();
        user.setId(id);
        user.setAvatar(fileURL);
        baseMapper.updateById(user);

    }

    @Override
    public int updateUserInfo(String token, UserVO userVO) throws ExpiredJwtException {
        Claims claims = JwtUtil.checkJWT(token);
        String id = (String) claims.get("id");
        log.info("userId: {}", id);
        User user = baseMapper.selectById(id);
        userVO.setId(id);
        BeanUtils.copyProperties(userVO, user);
        return baseMapper.updateById(user);

    }

    /**
     * 用户登录
     *
     * @param phone
     * @param password
     * @return
     */
    @Override
    public String login(String phone, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        //校验手机号是否正确
        User user = userMapper.selectOne(queryWrapper);
        if (ObjectUtils.isEmpty(user)) {
            throw new CustomException(Constants.LOGIN_ACCOUNT_ERROR);
        }
        //根据手机号获取密码，进行密码校验，如果不相等，密码错误
        boolean isMatch = SecurityUtils.matchesPassword(password, user.getPassword());
        if (!isMatch) {
            throw new CustomException("手机号或密码错误");
        }
        //更新最后一次登陆时间
        baseMapper.updateLoginTime(user.getId(), DateUtil.date());

        //如果用户合法，验证通过，将用户信息写成token，返回
        String token = JwtUtil.geneJsonWebToken(user);
        return token;
    }

    @Override
    public UserVO getUserInfoFromToken(String token) throws ExpiredJwtException {
        log.warn("head: {}", token);
        UserVO userVO = new UserVO();
        Claims claims = JwtUtil.checkJWT(token);
        if (claims == null) {
            throw new CustomException("用户认证过期，请重新登录");
        }
        String id = (String) claims.get("id");
        // 根据id查询数据
        User user = baseMapper.selectById(id);
        //封装VO
        BeanUtils.copyProperties(user, userVO);
        //返回结果
        return userVO;
    }

    /**
     * 修改密码
     *
     * @param userName
     * @param password
     * @return
     */
    @Override
    public int resetUserPwd(String userName, String password) {
        password = SecurityUtils.encryptPassword(password);
        return userMapper.resetUserPwd(userName, password);
    }

    /**
     * 用户注册
     *
     * @param registerVO
     * @return
     */
    @Override
    public int register(RegisterVO registerVO) {
        if (registerVO == null) return -1;
        User user = new User();
        if (StringUtils.isBlank(registerVO.getCity())) {
            throw new CustomException("用户收货地址不能为空");
        }
        if (StringUtils.isBlank(registerVO.getPhone())) {
            throw new CustomException("用户手机号不能为空");
        }
        if (StringUtils.isBlank(registerVO.getPassword())) {
            throw new CustomException("用户密码不能为空");
        }
        if (StringUtils.isBlank(registerVO.getNickname())) {
            throw new CustomException("用户昵称不能为空");
        }
        //校验手机号是否已注册
        User unique = baseMapper.checkPhoneUnique(registerVO.getPhone());
        if (unique != null) {
            throw new CustomException("该手机号已存在，请直接登录");
        }
        //校验用户昵称是否重复
        int count = baseMapper.checkUserNameUnique(registerVO.getNickname());
        if (count > 0) {
            throw new CustomException("该昵称已存在,再换一个吧");
        }
        BeanUtils.copyProperties(registerVO, user);
        //设置用户头像
        user.setAvatar("https://pic1.zhimg.com/80/v2-6afa72220d29f045c15217aa6b275808_720w.jpg?source=1940ef5c");
        //用户密码加密
        user.setPassword(SecurityUtils.encryptPassword(registerVO.getPassword()));
        return userMapper.insert(user);
    }

    /**
     * 修改密码
     *
     * @param user
     * @return
     */
    @Override
    public int resetPwd(User user) {
        return userMapper.updateById(user);
    }

    /**
     * 校验手机号是否唯一
     *
     * @param user
     * @return
     */
    @Override
    public String checkPhoneUnique(User user) {
        String userId = StringUtils.isNull(user.getId()) ? "" : user.getId();
        User info = userMapper.checkPhoneUnique(user.getPhone());
        if (StringUtils.isNotNull(info) && !info.getId().equals(userId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}