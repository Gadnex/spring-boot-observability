package net.binarypaper.notificationservice.order;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItem {
    
    private Long productId;

    private String productName;

    private BigDecimal unitPrice;

    private Long quantity;
}