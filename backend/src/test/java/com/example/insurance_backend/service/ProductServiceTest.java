package com.example.insurance_backend.service;

import com.example.insurance_backend.dto.SearchProductRequest;
import com.example.insurance_backend.mapper.InsuranceProductMapper;
import com.example.insurance_backend.model.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

	
//	findById() — 測試找到產品和找不到產品的情況
//
//	findAll() — 測試回傳所有產品
//
//	searchProducts() — 測試依條件搜尋
    @Mock
    private InsuranceProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById_found() {
        Product mockProduct = new Product();
        mockProduct.setId(1);
        mockProduct.setName("Test Product");

        when(productMapper.findById(1)).thenReturn(mockProduct);

        Product result = productService.findById(1);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        verify(productMapper, times(1)).findById(1);
    }

    @Test
    void findById_notFound() {
        when(productMapper.findById(99)).thenReturn(null);

        Product result = productService.findById(99);

        assertNull(result);
        verify(productMapper, times(1)).findById(99);
    }

    @Test
    void findAll_success() {
        Product p1 = new Product();
        p1.setId(1);
        p1.setName("P1");

        Product p2 = new Product();
        p2.setId(2);
        p2.setName("P2");

        when(productMapper.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Product> result = productService.findAll();

        assertEquals(2, result.size());
        verify(productMapper, times(1)).findAll();
    }

    @Test
    void searchProducts_withConditions() {
        SearchProductRequest req = new SearchProductRequest();
        req.setType("Life");
        req.setCurrency("USD");
        req.setIsSoldout(true);
        req.setMinAge(20);
        req.setMaxAge(60);

        Product mockProduct = new Product();
        mockProduct.setId(1);
        mockProduct.setName("Life Insurance");

        when(productMapper.searchProducts(anyMap())).thenReturn(Collections.singletonList(mockProduct));

        List<Product> result = productService.searchProducts(req);

        assertEquals(1, result.size());
        assertEquals("Life Insurance", result.get(0).getName());
        verify(productMapper, times(1)).searchProducts(anyMap());
    }
}
