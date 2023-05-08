package net.binarypaper.productservice.product;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UC_PRODUCT_NAME", columnNames = { "NAME" })
})
@Schema(description = "A product for which the stock level is managed")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = { Views.List.class, Views.View.class, Views.Update.class }, message = "{Product.id.NotNull}")
    @JsonView({
            Views.List.class,
            Views.View.class,
            Views.Update.class
    })
    @Schema(description = "The unique ID of the product", example = "1")
    private Long id;

    @NotNull(groups = { Views.Add.class, Views.List.class, Views.View.class,
            Views.Update.class }, message = "{Product.name.NotNull}")
    @Size(groups = { Views.Add.class, Views.List.class, Views.View.class,
            Views.Update.class }, min = 3, max = 100, message = "{Product.name.Size}")
    @JsonView({
            Views.List.class,
            Views.View.class,
            Views.Add.class,
            Views.Update.class
    })
    @Schema(description = "The name of the product", example = "My Product")
    private String name;

    @NotNull(groups = { Views.Add.class, Views.List.class, Views.View.class,
            Views.Update.class }, message = "{Product.price.NotNull}")
    @DecimalMin(groups = { Views.Add.class, Views.List.class, Views.View.class,
            Views.Update.class }, value = "0.00", message = "{Product.price.DecimalMin}")
    @JsonView({
            Views.List.class,
            Views.View.class,
            Views.Add.class,
            Views.Update.class
    })
    @Schema(description = "The price of the product", example = "9.99")
    private BigDecimal price;

    @NotNull(groups = { Views.Add.class, Views.List.class, Views.View.class,
            Views.Update.class }, message = "{Product.quantity.NotNull}")
    @Min(groups = { Views.Add.class, Views.List.class, Views.View.class,
            Views.Update.class }, value = 0, message = "{Product.quantity.Min}")
    @JsonView({
            Views.List.class,
            Views.View.class,
            Views.Add.class,
            Views.Update.class
    })
    @Schema(description = "The name of the product", example = "10")
    private Long quantity;

    /**
     * Field to demo the save of a sleuth trace ID to a DB
     */
    @JsonIgnore
    private String traceId;

    public void adjustQuantity(Long delta) {
        if (quantity + delta < 0) {
            throw new IllegalArgumentException("PR002");
        }
        quantity = quantity + delta;
    }

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