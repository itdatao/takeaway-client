package com.ruoyi.web.controller;

import com.google.gson.Gson;
import com.ruoyi.web.domain.User;
import com.ruoyi.web.service.IUserService;
import com.ruoyi.web.utils.ConstantPropertiesUtils;
import com.ruoyi.web.utils.HttpClientUtils;
import com.ruoyi.web.utils.JwtUtil;
import com.ruoyi.web.utils.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URLEncoder;
import java.util.HashMap;

/**
 * 客户端微信登录接口
 *
 * @Author Huhuitao
 * @Date 2021/2/3 15:23
 */
@CrossOrigin
@Controller
@RequestMapping("/ucenter/wx")
public class WxController {

    @Autowired
    private IUserService userService;


    // 微信登陆
    @ApiOperation("生成微信登录二维码")
    @GetMapping("login")
    public String geneOrConnect() {
        //定义baseURL授权URL
        String baseURL = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        //回调地址
        String redirectUrl = ConstantPropertiesUtils.REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String state = "atonline";
        System.out.println("state: " + state);
        String qrcodeUrl = String.format(
                baseURL,
                ConstantPropertiesUtils.APP_ID,
                redirectUrl,
                state);
        return "redirect:" + qrcodeUrl;
    }

    @ApiOperation("扫描二维码回调方法")
    @GetMapping("callback")
    public String callback(String code, String state) {
        System.out.println(code);
        System.out.println(state);

        //从redis中将state获取出来，和当前传入的state作比较
        //如果一致则放行，如果不一致则抛出异常：非法访问

        //向认证服务器发送请求换取access_token
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";
        //拼接请求地址
        String accessTokenUrl = String.format(baseAccessTokenUrl,
                ConstantPropertiesUtils.APP_ID,
                ConstantPropertiesUtils.APP_SECRET,
                code);

        //得到凭证结果和openid
        String result = null;
        try {
            result = HttpClientUtils.get(accessTokenUrl);
            System.out.println("accessToken=============" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        HashMap tokenMap = gson.fromJson(result, HashMap.class);
        String access_token = (String) tokenMap.get("access_token");
        String openid = (String) tokenMap.get("openid");
        //根据openid判断是不是新用户，如果是是新用户需要将用户信息添加到数据库，不是新用户直接登陆
        User user = userService.getByOpenId(openid);
        //Member member = memberService.getByOpenId(openid);
        //访问微信的资源服务器，获取用户信息
        String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                "?access_token=%s" +
                "&openid=%s";
        //新用户注册
        String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
        String resultUserInfo = null;
        try {
            resultUserInfo = HttpClientUtils.get(userInfoUrl);
            System.out.println("resultUserInfo==========" + resultUserInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //解析json
        HashMap<String, Object> mapUserInfo = gson.fromJson(resultUserInfo, HashMap.class);
        String nickname = (String) mapUserInfo.get("nickname");
        String headimgurl = (String) mapUserInfo.get("headimgurl");
        String city = (String) mapUserInfo.get("city");
        Double sex = (Double) mapUserInfo.get("sex");
        int sex2 = sex.intValue();

        if (sex2==2)sex2=1;
        else sex2 = 0;

        if (user == null) {
            //向数据库中插入一条记录
            user = new User();
            user.setNickname(nickname);
            user.setOpenid(openid);
            user.setAvatar(headimgurl);
            user.setCity(city);
            user.setSex(sex2);
            String encryptPassword = SecurityUtils.encryptPassword("123456");
            user.setPassword(encryptPassword);
            userService.save(user);
        }

        String token = JwtUtil.geneJsonWebToken(user);
        System.out.println("token: "+token);
        return "redirect:http://localhost:85?token="+token;
    }

}
