package com.ruoyi.web.controller;

import com.ruoyi.web.common.AjaxResult;
import com.ruoyi.web.dto.PayResultDTO;
import com.ruoyi.web.service.PayService;
import com.ruoyi.web.vo.PayInfoVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

/**
 * 客户端支付接口
 *
 * @Author Huhuitao
 * @Date 2021/2/3 15:23
 */
@CrossOrigin
@RestController
@RequestMapping("/pay")
public class PayController {

    @Autowired
    @Qualifier("PayServiceImpl")
    private PayService payService;

    //获取支付状态
    @ApiOperation("获取支付状态")
    @GetMapping("/status/{orderId}")
    public AjaxResult getPayStatus(@PathVariable String orderId){
        PayResultDTO payStatus = payService.getPayStatus(orderId);
        return AjaxResult.success("支付状态对象",payStatus);
    }


    @ApiOperation("返回支付二维码和支付信息")
    @GetMapping("/qr/{orderId}")
    public AjaxResult getPayInfo(@PathVariable String orderId) {
        PayInfoVO payInfoVO = payService.getPayInfo(orderId);
        return AjaxResult.success("支付成功", payInfoVO);
    }

}
