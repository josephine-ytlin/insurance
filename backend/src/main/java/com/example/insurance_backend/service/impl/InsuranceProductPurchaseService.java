package com.example.insurance_backend.service.impl;

import com.example.insurance_backend.mapper.OrderMapper;
import com.example.insurance_backend.model.Order;
import com.example.insurance_backend.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class InsuranceProductPurchaseService implements PurchaseService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public String createOrderAndPayment(Long userId, Long productId, BigDecimal amount) throws Exception {
        // 建立訂單
        Order order = new Order();
        order.setUserId(userId);
        order.setProductId(productId);
        order.setAmount(amount);
        order.setCurrency("TWD");
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.insertOrder(order);

        // 模擬支付連結
        String paymentUrl = "https://payment-provider.com/checkout?order=" + order.getId();
        order.setPaymentUrl(paymentUrl);
        orderMapper.updateOrder(order);

        return paymentUrl;
    }

    @Override
    public boolean supports(String productType) {
        return "INSURANCE".equalsIgnoreCase(productType);
    }
}
