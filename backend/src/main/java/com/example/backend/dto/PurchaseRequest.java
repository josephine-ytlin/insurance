package com.example.backend.dto;

import java.math.BigDecimal;

public class PurchaseRequest {
    private Long userId;
    private Long productId;
    private String productType;
    private BigDecimal amount;

    // Getter / Setter
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
