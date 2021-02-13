package com.ruoyi.web.service;

import com.ruoyi.web.dto.PayResultDTO;
import com.ruoyi.web.vo.PayInfoVO;

/**
 * @Author Huhuitao
 * @Date 2021/2/9 11:33
 */
public interface PayService {

    PayInfoVO getPayInfo(String orderId);

    PayResultDTO getPayStatus(String orderId);
}
