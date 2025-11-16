package com.Ecom.EcomEngineAPI.dto;

import com.Ecom.EcomEngineAPI.entity.Address;
import com.Ecom.EcomEngineAPI.entity.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponseDTO {
    private Long orderId;
    private Long userId;
    private Address shippingAddress;
    private OrderStatus status;
    private Long totalAmount;
    private boolean paid;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<OrderItemResponseDTO> items;
}
