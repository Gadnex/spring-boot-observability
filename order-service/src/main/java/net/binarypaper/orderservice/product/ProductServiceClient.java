package net.binarypaper.orderservice.product;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.argument.StructuredArguments;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProductServiceClient {

    private WebClient productService;

    public ProductServiceClient(WebClient webClient,
            @Value("${application.product-service.url}") String productServiceUrl) {
        productService = webClient.mutate().baseUrl(productServiceUrl).build();
    }

    @NewSpan
    public Mono<Product> getProductById(Long productId) {
        log.info("getProductById API called",
                StructuredArguments.kv("productId", productId));
        return productService
                .get()
                .uri((uriBuilder) -> uriBuilder
                        .path("/{productId}")
                        .build(productId))
                .accept(MediaType.APPLICATION_JSON)
                // .retrieve()
                // .bodyToMono(Product.class);
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(Product.class);
                    } else {
                        return response.createException().flatMap(Mono::error);
                    }
                });
    }

}
