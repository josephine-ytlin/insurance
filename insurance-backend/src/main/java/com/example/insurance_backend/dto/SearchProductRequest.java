package com.example.insurance_backend.dto;

public class SearchProductRequest {
    private String type;
    private String currency;
    private Boolean isBonus;
    private Integer minAge;
    private Integer maxAge;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Boolean getIsBonus() { return isBonus; }
    public void setIsBonus(Boolean isBonus) { this.isBonus = isBonus; }

    public Integer getMinAge() { return minAge; }
    public void setMinAge(Integer minAge) { this.minAge = minAge; }

    public Integer getMaxAge() { return maxAge; }
    public void setMaxAge(Integer maxAge) { this.maxAge = maxAge; }
}
