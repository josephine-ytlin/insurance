package com.example.insurance_backend.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.insurance_backend.dto.ApiResponse;
import com.example.insurance_backend.dto.PurchaseRequest;
import com.example.insurance_backend.service.PurchaseService;

@RestController
@RequestMapping("/product")
public class PurchaseController {

    @Autowired
    private List<PurchaseService> purchaseServices;

    @PostMapping("/purchase")
    public ApiResponse<Map<String, String>> purchase(@RequestBody PurchaseRequest request) {
        try {
            Long userId = request.getUserId();
            Long productId = request.getProductId();
            BigDecimal amount = request.getAmount();
            String productType = request.getProductType();

            // 找到支援的服務
            PurchaseService service = purchaseServices.stream()
                    .filter(s -> s.supports(productType))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("不支援的商品類型"));

            String paymentUrl = service.createOrderAndPayment(userId, productId, amount);

            // 用 ApiResponse 回傳資料
            return new ApiResponse<>(true, "訂單建立成功", Map.of("paymentUrl", paymentUrl));

        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(false, e.getMessage(), null);
        }
    }

}
