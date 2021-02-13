package com.ruoyi.web.controller;

import com.ruoyi.web.common.AjaxResult;
import com.ruoyi.web.common.Constants;
import com.ruoyi.web.service.IUserService;
import com.ruoyi.web.utils.FileUploadUtils;
import com.ruoyi.web.utils.StringUtils;
import com.ruoyi.web.vo.LoginVO;
import com.ruoyi.web.vo.RegisterVO;
import com.ruoyi.web.vo.UserVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author Huhuitao
 * @Date 2021/2/4 20:56
 */
@CrossOrigin
@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    // 注册
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterVO registerVO) {
        int result = userService.register(registerVO);
        return result > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    // 手机号 密码 登录
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginVO loginVO) {
        String token = "";
        token = userService.login(loginVO.getPhone(), loginVO.getPassword());

        return AjaxResult.success("登录成功", token);
    }

    // 根据token获取用户
    @GetMapping("/info")
    public AjaxResult getUser(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            return AjaxResult.error("登录过期请重新登录");
        }
        UserVO userVO = userService.getUserInfoFromToken(token);
        return AjaxResult.success(userVO);
    }

    //上传头像
    @ApiOperation("上传头像")
    @PostMapping("/avatar")
    public AjaxResult uploadAvatar(MultipartFile file, HttpServletRequest request) throws IOException {
        if (file.isEmpty()) {
            return AjaxResult.error("没有选择文件");
        }
        String fileURL = FileUploadUtils.upload(Constants.AVATAR_PATH, file);
        fileURL = Constants.PREFIX_URL + fileURL;
        log.info("========fileURL==={}================", fileURL);
        //更新头像
        String token = request.getHeader("token");
        userService.updateAvatar(token, fileURL);
        return AjaxResult.success("avatar upload success", fileURL);
    }

    //修改用户信息
    @PutMapping("/info")
    @ApiOperation("修改用户信息")
    public AjaxResult updateUser(@RequestBody UserVO userVO, HttpServletRequest request) {
        String token = request.getHeader("token");
        int rowCount = userService.updateUserInfo(token, userVO);
        return rowCount > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    //修改地址
    @PutMapping("/address")
    @ApiOperation("修改用户收货地址")
    public AjaxResult updateAddress(@RequestParam("address") String address, HttpServletRequest request) {
        String token = request.getHeader("token");
        UserVO userVO = new UserVO();
        userVO.setCity(address);
        int rowCount = userService.updateUserInfo(token, userVO);
        return rowCount > 0 ? AjaxResult.success("修改成功",address) : AjaxResult.error("修改地址失败");
    }

    //修改用户密码
    @ApiOperation("修改用户密码")
    @PostMapping("/password")
    public AjaxResult resetPassword(@RequestParam("oldPassword") String oldPassword,
                                    @RequestParam("newPassword") String newPassword, HttpServletRequest request) {
        String token = request.getHeader("token");
        int rowCount = userService.updatePassword(oldPassword, newPassword, token);

        return rowCount > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    @ApiOperation("设置用户手机号")
    @PutMapping("/phone/{phone}")
    public AjaxResult bindPhone(@PathVariable String phone,HttpServletRequest request){
        String token = request.getHeader("token");
        int rowCount = userService.bindPhone(phone,token);
        return rowCount>0?AjaxResult.success("绑定手机号成功"):AjaxResult.error("绑定手机号失败！请稍后重试");
    }


}
