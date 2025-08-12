package com.example.insurance_backend.controller;

import com.example.insurance_backend.entity.InsuranceProduct;
import com.example.insurance_backend.service.InsuranceProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final InsuranceProductService productService;

    public ProductController(InsuranceProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "以產品id取得產品資訊")
    public ResponseEntity<?> getProductById(@PathVariable Integer id) {
        logger.info("取得產品資訊請求，產品id: {}", id);
        try {
            InsuranceProduct product = productService.findById(id);
            if (product != null) {
                return ResponseEntity.ok(product);
            } else {
                logger.warn("產品不存在，id: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("取得產品資訊時發生錯誤，id: {}", id, e);
            return ResponseEntity.status(500).body(Map.of("message", "伺服器錯誤，請稍後再試"));
        }
    }

    @PostMapping
    @Operation(summary = "取得所有產品資訊")
    public ResponseEntity<?> getAllProducts() {
        logger.info("取得所有產品資訊請求");
        try {
            List<InsuranceProduct> products = productService.findAll();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            logger.error("取得所有產品時發生錯誤", e);
            return ResponseEntity.status(500).body(Map.of("message", "伺服器錯誤，請稍後再試"));
        }
    }

    @PostMapping("/search")
    @Operation(summary = "依條件查詢商品")
    public ResponseEntity<?> searchProducts(@RequestBody Map<String, Object> req) {
        logger.info("依條件查詢商品請求，條件: {}", req);
        try {
            String type = (String) req.get("type");
            String currency = (String) req.get("currency");
            Boolean isBonus = req.get("isBonus") != null ? (Boolean) req.get("isBonus") : null;
            Integer minAge = req.get("minAge") != null ? (Integer) req.get("minAge") : null;
            Integer maxAge = req.get("maxAge") != null ? (Integer) req.get("maxAge") : null;

            List<InsuranceProduct> products = productService.searchProducts(type, currency, isBonus, minAge, maxAge);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            logger.error("依條件查詢商品時發生錯誤，條件: {}", req, e);
            return ResponseEntity.status(500).body(Map.of("message", "伺服器錯誤，請稍後再試"));
        }
    }
}
