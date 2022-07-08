package net.binarypaper.productservice.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.binarypaper.productservice.ClasspathProperties;
import net.binarypaper.productservice.DataIntegrityViolationExceptionHandler;
import net.logstash.logback.argument.StructuredArguments;

@RestController
@CrossOrigin(origins = { "${application.cors.origins}" })
@RequestMapping(path = "products", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Product API", description = "API to manage products")
@Slf4j
public class ProductController {

    private ProductRepository productRepository;

    private Properties errorMessageProperties;

    private DataIntegrityViolationExceptionHandler dataIntegrityViolationExceptionHandler;

    private Tracer tracer;

    public ProductController(ProductRepository productRepository, Tracer tracer) {
        this.productRepository = productRepository;
        errorMessageProperties = ClasspathProperties.load("ErrorMessages.properties");
        dataIntegrityViolationExceptionHandler = new DataIntegrityViolationExceptionHandler(errorMessageProperties);
        dataIntegrityViolationExceptionHandler.addConstraintValidation("UC_PRODUCT_NAME", "{PR001}");
        this.tracer = tracer;
    }

    @GetMapping
    @JsonView(Product.Views.List.class)
    @Operation(summary = "Get a list of products", description = "Get a list of products sorted by name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of products returned")
    })
    public List<Product> getAllProducts() {
        log.info("getAllProducts called");
        return productRepository.findAll(Sort.by("name"));
    }

    @GetMapping(path = "{product-id}")
    @JsonView(Product.Views.View.class)
    @Operation(summary = "Get product details by ID", description = "Get the product details by product ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product details returned"),
            @ApiResponse(responseCode = "404", description = "Invalid productId", content = @Content)
    })
    public Product getProductById(
            @PathVariable(name = "product-id") @Parameter(description = "The ID of the product to return", example = "1") Long productId,
            @RequestHeader Map<String, Object> headers) {
        log.info("getProductById called",
                StructuredArguments.kv("productId", productId),
                StructuredArguments.kv("headers", headers));
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The productId is invalid");
        }
        return product.get();
    }

    @PostMapping

    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    @JsonView(Product.Views.View.class)
    @Operation(summary = "Add a product to the database", description = "Add a product to the database. The unique product ID will be generated by the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "The product was added"),
            @ApiResponse(responseCode = "400", description = "Invalid product details", content = @Content)
    })
    public Product addProduct(
            @RequestBody @Validated(Product.Views.Add.class) @JsonView(Product.Views.Add.class) Product product) {
        log.info("addProduct called",
                StructuredArguments.kv("product", product));
        // Get the traceId from tracer as set in product
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            product.setTraceId(currentSpan.context().traceId());
        }
        try {
            productRepository.save(product);
        } catch (DataIntegrityViolationException ex) {
            dataIntegrityViolationExceptionHandler.handleException(ex);
        }
        return product;
    }

    @PatchMapping(path = "{product-id}/quantity")
    @JsonView(Product.Views.View.class)
    @Operation(summary = "Get product details by ID", description = "Get the product details by product ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product quantity updated and product details returned"),
            @ApiResponse(responseCode = "404", description = "Invalid productId", content = @Content)
    })
    public Product updateQuantity(
            @PathVariable(name = "product-id") @Parameter(description = "The ID of the product to update", example = "1") Long productId,
            @RequestParam @Parameter(description = "The delta value with which to modify the product quantity", example = "-1") Long delta) {
        log.info("updateQuantity called",
                StructuredArguments.kv("productId", productId),
                StructuredArguments.kv("delta", delta));
        Product product = getProductById(productId, new HashMap<>());
        try {
            product.adjustQuantity(delta);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    errorMessageProperties.getProperty(ex.getMessage()));
        }
        productRepository.save(product);
        return product;
    }

}