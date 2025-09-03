package com.example.backend.service;

import java.math.BigDecimal;

public interface PurchaseService {
    /**
     * 建立訂單並返回支付連結
     */
    String createOrderAndPayment(Long userId, Long productId, BigDecimal amount) throws Exception;

    /**
     * 判斷該服務是否支援某個商品類型
     */
    boolean supports(String productType);
}
