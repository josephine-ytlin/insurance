package com.example.insurance_backend.dto;

public class ProductResponse {
	private Integer id;
    private String name;
    private String type;
    private String currency;
    private Boolean isBonus;
    private Integer minAge;
    private Integer maxAge;
    
	public ProductResponse(Integer id, String name, String type, String currency, Boolean isBonus, Integer minAge,
			Integer maxAge) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.currency = currency;
		this.isBonus = isBonus;
		this.minAge = minAge;
		this.maxAge = maxAge;
	}
	public Integer getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
	public String getCurrency() {
		return currency;
	}
	public Boolean getIsBonus() {
		return isBonus;
	}
	public Integer getMinAge() {
		return minAge;
	}
	public Integer getMaxAge() {
		return maxAge;
	}

}
