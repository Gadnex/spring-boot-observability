package net.binarypaper.orderservice.order;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.binarypaper.orderservice.order.Order.Views;

@Data
@Schema(description = "An item ordered as part of an order")
public class OrderItem {

    @NotNull(groups = { Views.Add.class, Views.List.class, Views.View.class,
            Views.Update.class }, message = "{OrderItem.productId.NotNull}")
    @JsonView({
            Views.List.class,
            Views.View.class,
            Views.Add.class,
            Views.Update.class
    })
    @Schema(description = "The unique product ID of the product being ordered", example = "1")
    private Long productId;

    @NotNull(groups = { Views.List.class, Views.View.class,
            Views.Update.class }, message = "{OrderItem.productName.NotNull}")
    @Size(groups = { Views.List.class, Views.View.class,
            Views.Update.class }, min = 3, max = 100, message = "{OrderItem.productName.Size}")
    @JsonView({
            Views.List.class,
            Views.View.class,
            Views.Update.class
    })
    @Schema(description = "The name of the product being ordered", example = "My Product")
    private String productName;

    @NotNull(groups = { Views.List.class, Views.View.class,
            Views.Update.class }, message = "{OrderItem.unitPrice.NotNull}")
    @DecimalMin(groups = { Views.List.class, Views.View.class,
            Views.Update.class }, value = "0.00", message = "{OrderItem.unitPrice.DecimalMin}")
    @JsonView({
            Views.List.class,
            Views.View.class,
            Views.Update.class
    })
    @Schema(description = "The unit price of the product being ordered", example = "9.99")
    private BigDecimal unitPrice;

    @NotNull(groups = { Views.List.class, Views.View.class,
            Views.Update.class }, message = "{OrderItem.quantity.NotNull}")
    @Min(groups = { Views.List.class, Views.View.class,
            Views.Update.class }, value = 0, message = "{OrderItem.quantity.Min}")
    @JsonView({
            Views.List.class,
            Views.View.class,
            Views.Update.class
    })
    @Schema(description = "The name of the product", example = "10")
    private Long quantity;

}