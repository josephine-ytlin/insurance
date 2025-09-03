package com.example.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.backend.model.Order;

import java.util.List;

@Mapper
public interface OrderMapper {
    // 新增訂單
    int insertOrder(Order order);

    // 根據 ID 查詢
    Order selectById(@Param("id") Long id);

    // 根據使用者查詢訂單列表
    List<Order> selectByUserId(@Param("userId") Long userId);

    // 更新訂單（例如更新支付狀態、paymentUrl）
    int updateOrder(Order order);
}
