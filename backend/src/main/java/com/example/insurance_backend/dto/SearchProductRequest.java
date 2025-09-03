package com.example.insurance_backend.dto;

public class SearchProductRequest {
    private String type;
    private String currency;
    private Boolean isSoldout;
    private Integer minPrice;
    private Integer maxPrice;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Boolean getIsSoldout() { return isSoldout; }
    public void setIsSoldout(Boolean isSoldout) { this.isSoldout = isSoldout; }

    public Integer getMinPrice() { return minPrice; }
    public void setMinAge(Integer minAge) { this.minPrice = minAge; }

    public Integer getMaxPrice() { return maxPrice; }
    public void setMaxAge(Integer maxAge) { this.maxPrice = maxAge; }
}
