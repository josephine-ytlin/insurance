package com.example.backend.controller;

import com.example.backend.dto.ApiResponse;
//import com.example.backend.dto.ProductResponse;
import com.example.backend.dto.SearchProductRequest;
import com.example.backend.model.Product;
import com.example.backend.service.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "以產品id取得產品資訊")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Integer id) {
        Product product = productService.findById(id);
        if (product != null) {
            return ResponseEntity.ok(new ApiResponse<>(true, "成功取得產品", product));
        }
        return ResponseEntity.ok(new ApiResponse<>(false, "產品不存在", null));
    }

    @GetMapping
    @Operation(summary = "取得所有產品資訊")
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        List<Product> dtos = productService.findAll();
        return ResponseEntity.ok(new ApiResponse<>(true, "成功取得所有產品", dtos));
    }

    @PostMapping("/search")
    @Operation(summary = "依條件查詢商品")
    public ResponseEntity<ApiResponse<List<Product>>> searchProducts(@RequestBody SearchProductRequest req) {
        List<Product> dtos = productService.searchProducts(req);
        return ResponseEntity.ok(new ApiResponse<>(true, "查詢成功", dtos));
    }

//    private ProductResponse toDto(Product p) {
//        return new ProductResponse(
//                p.getId(),
//                p.getName(),
//                p.getType(),
//                p.getCurrency(),
//                p.getIsSoldout(),
//                p.getPrice(),
//                p.getPrice()
//        );
//    }
}
