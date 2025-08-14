package com.example.insurance_backend.service;

import com.example.insurance_backend.dto.SearchProductRequest;
import com.example.insurance_backend.entity.InsuranceProduct;
import com.example.insurance_backend.mapper.InsuranceProductMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final InsuranceProductMapper productMapper;

    public ProductService(InsuranceProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public InsuranceProduct findById(Integer id) {
        logger.info("取得產品資訊，產品id: {}", id);
        InsuranceProduct product = productMapper.findById(id);
        if (product == null) {
            logger.warn("找不到產品，id: {}", id);
        }
        return product;
    }

    public List<InsuranceProduct> findAll() {
        logger.info("取得所有產品資訊");
        List<InsuranceProduct> products = productMapper.findAll();
        logger.debug("取得 {} 筆產品", products.size());
        return products;
    }

    public List<InsuranceProduct> searchProducts(SearchProductRequest req) {
        logger.info("依條件查詢商品，條件: {}", req);

        Map<String, Object> params = new HashMap<>();
        params.put("type", req.getType());
        params.put("currency", req.getCurrency());
        params.put("isBonus", req.getIsBonus());
        params.put("minAge", req.getMinAge());
        params.put("maxAge", req.getMaxAge());

        List<InsuranceProduct> products = productMapper.searchProducts(params);
        logger.debug("查詢結果 {} 筆", products.size());
        return products;
    }
}
