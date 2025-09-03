package com.example.insurance_backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.insurance_backend.model.Product;

import java.util.List;
import java.util.Map;

@Mapper
public interface InsuranceProductMapper {
    Product findById(@Param("id") Integer id);
    List<Product> findAll();
    List<Product> searchProducts(Map<String, Object> params);
}