package com.example.insurance_backend.controller;

import com.example.insurance_backend.dto.ApiResponse;
import com.example.insurance_backend.dto.ProductResponse;
import com.example.insurance_backend.dto.SearchProductRequest;
import com.example.insurance_backend.entity.InsuranceProduct;
import com.example.insurance_backend.service.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "以產品id取得產品資訊")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Integer id) {
        InsuranceProduct product = productService.findById(id);
        if (product != null) {
            return ResponseEntity.ok(new ApiResponse<>(true, "成功取得產品", toDto(product)));
        }
        return ResponseEntity.ok(new ApiResponse<>(false, "產品不存在", null));
    }

    @GetMapping
    @Operation(summary = "取得所有產品資訊")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        List<ProductResponse> dtos = productService.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "成功取得所有產品", dtos));
    }

    @PostMapping("/search")
    @Operation(summary = "依條件查詢商品")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> searchProducts(@RequestBody SearchProductRequest req) {
        List<ProductResponse> dtos = productService.searchProducts(req)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "查詢成功", dtos));
    }

    private ProductResponse toDto(InsuranceProduct p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getType(),
                p.getCurrency(),
                p.getIsBonus(),
                p.getMinAge(),
                p.getMaxAge()
        );
    }
}
