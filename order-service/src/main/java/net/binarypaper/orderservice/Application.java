package net.binarypaper.orderservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// Need to inject WebClient.Builder for trace propagation to work
	@Bean
	public WebClient webClient(
			WebClient.Builder builder,
			@Value("${application.product-service.url}") String productServiceUrl) {
		return builder.baseUrl(productServiceUrl).build();
	}

	// Required for @Observed annotation for metrics and tracing to work
	@Bean
	public ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
		return new ObservedAspect(observationRegistry);
	}

	// By default spring-kafka creates topics on the kafka server if they do not
	// exist.
	// These topics will by default have 1 partition and a replication factor of 1.
	// Since we have a kafka consumer in the notification-service with 4 consumer
	// thread,
	// it means that the single partition will be assigned to one of the thread.
	// This thread will process all of the messages and the other threads will idle.
	// To fix this we need to create the topic with at least 4 threads.
	// Uncomment the @Bean below to do this.
	// Remember to recreate the kafka container to remove the created topic.

	// @Bean
	// public NewTopic orderQueue(@Value("${application.kafka.topic}") String
	// topicName) {
	// return TopicBuilder.name(topicName)
	// .partitions(4)
	// .replicas(1)
	// .build();
	// }

}