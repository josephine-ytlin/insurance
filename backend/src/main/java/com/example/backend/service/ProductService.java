package com.example.backend.service;

import com.example.backend.dto.SearchProductRequest;
import com.example.backend.mapper.ProductMapper;
import com.example.backend.model.Product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public Product findById(Integer id) {
        logger.info("取得產品資訊，產品id: {}", id);
        Product product = productMapper.findById(id);
        if (product == null) {
            logger.warn("找不到產品，id: {}", id);
        }
        return product;
    }

    public List<Product> findAll() {
        logger.info("取得所有產品資訊");
        List<Product> products = productMapper.findAll();
        logger.debug("取得 {} 筆產品", products.size());
        return products;
    }

    public List<Product> searchProducts(SearchProductRequest req) {
        logger.info("依條件查詢商品，條件: {}", req);

        Map<String, Object> params = new HashMap<>();
        params.put("type", req.getType());
        params.put("currency", req.getCurrency());
        params.put("isSoldout", req.getIsSoldout());
        params.put("minPrice", req.getMinPrice());
        params.put("maxPrice", req.getMaxPrice());

        List<Product> products = productMapper.searchProducts(params);
        logger.debug("查詢結果 {} 筆", products.size());
        return products;
    }
}
