package com.ruoyi.web.common;

/**
 * 通用常量信息
 *
 * @author ruoyi
 */
public class Constants {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";
    /**
     * 登录用户账号不正确
     */
    public static final String LOGIN_ACCOUNT_ERROR = "user_account_error";
    /**
     * 用户密码不正确
     */
    public static final String LOGIN_PASSWORD_ERROR = "user_password_error";
    //参数错误
    public static final String ARGV_ERROR = "参数错误";
    //订单状态 已取消
    public static final Integer ORDER_STATUS_CANCELED = -1;
    //订单状态 未审核
    public static final Integer ORDER_STATUS_NO_CHECK = 0;
    //订单状态 已审核
    public static final Integer ORDER_STATUS_CHECK_PASS = 1;
    //订单状态 配送中
    public static final Integer ORDER_STATUS_IN_DELIVERY = 2;
    //订单状态 已完成
    public static final Integer ORDER_STATUS_FINISH = 3;

    //交易状态 已支付
    public static final Integer PAY_STATUS_PAID = 1;
    //交易状态 未支付
    public static final Integer PAY_STATUS_UNPAID = 0;

    //文件头像上传路径
    public static final String AVATAR_PATH = "D:/ruoyi/uploadPath/avatar";
    public static final String FILE_PATH = "D:/ruoyi/uploadPath";

    //后台服务器地址
    public static final String PREFIX_URL = "http://localhost:8080/takeaway";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 用户ID
     */
    public static final String JWT_USERID = "userid";

    /**
     * 用户名称
     */
    public static final String JWT_USERNAME = "sub";

    /**
     * 用户头像
     */
    public static final String JWT_AVATAR = "avatar";

    /**
     * 创建时间
     */
    public static final String JWT_CREATED = "created";

    /**
     * 用户权限
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";
}
