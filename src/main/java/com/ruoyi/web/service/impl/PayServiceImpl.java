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
                "    \"out_trade_no\":\"" + orderId + "\"," +//???????????????
                "    \"total_amount\":\"" + order.getOrderPrice().setScale(2, RoundingMode.CEILING) + "\"," +
                "    \"subject\":\"" + shopSellName + "?????????" + "\"," +
                "    \"store_id\":\"00100\"," + // (??????) ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                "    \"timeout_express\":\"5m\"}");//?????????????????????????????????
        AlipayTradePrecreateResponse response = null;
        //??????????????????URL
        String qrCode = "";
        try {
            response = alipayClient.execute(request);
            if (!response.isSuccess()) {
                throw new CustomException("???????????????????????????:" + response.getMsg());
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


    //??????????????????
    public PayResultDTO getPayStatus(String orderId) {
        log.info("status: ========= {}",orderId);
        //??????API?????????request???
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
                // ????????????????????????
                payResultDTO.setStatus(PayStatusEnum.WAIT_SCANNED.getCode());
                payResultDTO.setMsg("????????????");
                return payResultDTO;
            }
            payResultDTO.setThirdPartId(response.getTradeNo());
            switch (response.getTradeStatus()) {
                case "WAIT_BUYER_PAY":
                    payResultDTO.setStatus(PayStatusEnum.UN_PROCESSED.getCode());
                    payResultDTO.setMsg("??????????????????????????????");
                    break;
                case "TRADE_CLOSED":
                    payResultDTO.setStatus(PayStatusEnum.SHIPPING.getCode());
                    payResultDTO.setMsg("???????????????");
                    break;
                case "TRADE_SUCCESS":
                    payResultDTO.setStatus(PayStatusEnum.PROCESSED.getCode());
                    payResultDTO.setAmount(new BigDecimal(response.getTotalAmount()));
                    payResultDTO.setMsg("????????????");
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
            payResultDTO.setMsg("????????????????????????:" + e.getErrMsg());
            return payResultDTO;
        }
    }
}
