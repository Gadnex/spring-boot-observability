package net.binarypaper.orderservice.order;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "An order for a collection of products")
public class Order {

    @NotNull(groups = { Views.List.class, Views.View.class,
            Views.Update.class }, message = "{Order.orderNumber.NotNull}")
    @JsonView({
            Views.List.class,
            Views.View.class,
            Views.Update.class,
    })
    @Schema(description = "The unique order number for the order", example = "86b15f28-832e-4d8d-b936-99962c1ba9bc")
    private UUID orderNumber;

    @NotNull(groups = { Views.Add.class, Views.List.class, Views.View.class,
            Views.Update.class }, message = "{Order.clientEmail.NotNull}")
    @Email(groups = { Views.Add.class, Views.List.class, Views.View.class,
            Views.Update.class }, message = "{Order.clientEmail.Email}")
    @JsonView({
            Views.List.class,
            Views.View.class,
            Views.Add.class,
            Views.Update.class,
    })
    @Schema(description = "The email address of the client who placed the order", example = "client@example.com")
    private String clientEmail;

    @NotNull(groups = { Views.Add.class, Views.List.class, Views.View.class,
            Views.Update.class }, message = "{Order.orderItems.NotNull}")
    @Size(groups = { Views.Add.class, Views.List.class, Views.View.class,
            Views.Update.class }, min = 1, max = 10, message = "{Order.orderItems.Size}")
    @Valid
    @JsonView({
            Views.List.class,
            Views.View.class,
            Views.Add.class,
            Views.Update.class,
    })
    @Schema(description = "The list of items ordered")
    private List<OrderItem> orderItems;

    public interface Views {

        public interface List {
        }

        public interface View {
        }

        public interface Add {
        }

        public interface Update {
        }
    }

}