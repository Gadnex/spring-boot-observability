package net.binarypaper.notificationservice.order;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class Order {
    
    private UUID orderNumber;

    private String clientEmail;

    private List<OrderItem> orderItems;
}