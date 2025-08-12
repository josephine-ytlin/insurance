package com.example.insurance_backend.mapper;

import com.example.insurance_backend.entity.InsuranceProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface InsuranceProductMapper {
    InsuranceProduct findById(@Param("id") Integer id);
    List<InsuranceProduct> findAll();
    List<InsuranceProduct> searchProducts(Map<String, Object> params);
}