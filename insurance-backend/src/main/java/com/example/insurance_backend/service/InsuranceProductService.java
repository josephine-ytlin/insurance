package com.example.insurance_backend.service;

import com.example.insurance_backend.entity.InsuranceProduct;
import com.example.insurance_backend.mapper.InsuranceProductMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InsuranceProductService {
	
    private InsuranceProductMapper productMapper;
    
    

    public InsuranceProductService(InsuranceProductMapper productMapper) {
		super();
		this.productMapper = productMapper;
	}

	public InsuranceProduct findById(Integer id) {
        return productMapper.findById(id);
    }

    public List<InsuranceProduct> findAll() {
        return productMapper.findAll();
    }

    public List<InsuranceProduct> searchProducts(String type, String currency, Boolean isBonus, Integer minAge, Integer maxAge) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("currency", currency);
        params.put("isBonus", isBonus);
        params.put("minAge", minAge);
        params.put("maxAge", maxAge);
        return productMapper.searchProducts(params);
    }
}