package com.ruoyi.web.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.qrcode.QrCodeException;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.ruoyi.web.common.Constants;
import com.ruoyi.web.domain.Order;
import com.ruoyi.web.domain.Shop;
import com.ruoyi.web.dto.PayResultDTO;
import com.ruoyi.web.enums.PayStatusEnum;
import com.ruoyi.web.exception.CustomException;
import com.ruoyi.web.mapper.OrderMapper;
import com.ruoyi.web.mapper.ShopMapper;
import com.ruoyi.web.service.PayService;
import com.ruoyi.web.vo.PayInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Author Huhuitao
 * @Date 2021/2/9 11:39
 */
@Service("PayServiceImpl")
@Slf4j
public class PayServiceImpl implements PayService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ShopMapper shopMapper;

    @Value("${alipay.app-id}")
    private String appId;

    @Value("${alipay.private-key}")
    private String privateKey;

    @Value("${alipay.public-key}")
    private String publicKey;

    @Value("${alipay.gateway}")
    private String gateway;

    private AlipayClient alipayClient;

    @PostConstruct
    public void init() {
        alipayClient = new DefaultAlipayClient(gateway,
                appId, privateKey, "json", "utf-8", publicKey, "RSA2");
    }

    @Override
    public PayInfoVO getPayInfo(String orderId) {
        log.info("info ============= {}",orderId);
        Order order = orderMapper.selectById(orderId);
        String sellId = order.getSellId();
        Shop shop = shopMapper.selectById(sellId);
        String shopSellName = shop.getSellName();
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setBizContent("{" +
                "    \"out_trade_no\":\"" + orderId + "\"," +//商户订单号
                "    \"total_amount\":\"" + order.getOrderPrice().setScale(2, RoundingMode.CEILING) + "\"," +
                "    \"subject\":\"" + shopSellName + "当面付" + "\"," +
                "    \"store_id\":\"00100\"," + // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
                "    \"timeout_express\":\"5m\"}");//订单允许的最晚付款时间
        AlipayTradePrecreateResponse response = null;
        //支付宝二维码URL
        String qrCode = "";
        try {
            response = alipayClient.execute(request);
            if (!response.isSuccess()) {
                throw new CustomException("生成支付宝订单失败:" + response.getMsg());
            }
            qrCode = response.getQrCode();
        } catch (AlipayApiException e) {
            throw new CustomException(e.getMessage());
        }
        PayInfoVO payInfoVO = new PayInfoVO();
        payInfoVO.setOrderId(order.getOrderId());
        payInfoVO.setShopName(shopSellName);
        payInfoVO.setUrl(qrCode);
        log.info("payInfo: {}",payInfoVO);
        return payInfoVO;
    }


    //获取支付状态
    public PayResultDTO getPayStatus(String orderId) {
        log.info("status: ========= {}",orderId);
        //创建API对应的request类
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent("{" +
                "    \"out_trade_no\":\"" + orderId + "\"}");
        AlipayTradeQueryResponse response = null;
        Order order = new Order();
        order.setOrderId(orderId);
        PayResultDTO payResultDTO = new PayResultDTO();
        try {
            response = alipayClient.execute(request);
            if (!response.isSuccess()) {
                // 获取支付信息失败
                payResultDTO.setStatus(PayStatusEnum.WAIT_SCANNED.getCode());
                payResultDTO.setMsg("等待扫码");
                return payResultDTO;
            }
            payResultDTO.setThirdPartId(response.getTradeNo());
            switch (response.getTradeStatus()) {
                case "WAIT_BUYER_PAY":
                    payResultDTO.setStatus(PayStatusEnum.UN_PROCESSED.getCode());
                    payResultDTO.setMsg("已扫码，等待用户支付");
                    break;
                case "TRADE_CLOSED":
                    payResultDTO.setStatus(PayStatusEnum.SHIPPING.getCode());
                    payResultDTO.setMsg("支付已关闭");
                    break;
                case "TRADE_SUCCESS":
                    payResultDTO.setStatus(PayStatusEnum.PROCESSED.getCode());
                    payResultDTO.setAmount(new BigDecimal(response.getTotalAmount()));
                    payResultDTO.setMsg("支付成功");
                    order.setPayStatus(Constants.PAY_STATUS_PAID);
                    order.setPayTime(DateUtil.date());
                    orderMapper.updateById(order);
                    break;
                default:
                    break;
            }
            return payResultDTO;
        } catch (AlipayApiException e) {
            payResultDTO.setStatus(-1);
            payResultDTO.setMsg("查询订单状态错误:" + e.getErrMsg());
            return payResultDTO;
        }
    }
}
