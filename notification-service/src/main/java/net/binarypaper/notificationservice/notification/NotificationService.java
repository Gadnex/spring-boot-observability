package net.binarypaper.notificationservice.notification;

import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.binarypaper.notificationservice.order.Order;
import net.logstash.logback.argument.StructuredArguments;

@Service
@Slf4j
public class NotificationService {

    private SleepService sleepService;

    public NotificationService(SleepService sleepService) {
        this.sleepService = sleepService;
    }

    @KafkaListener(topics = { "${application.kafka.topic}" })
    @NewSpan
    public void processMessage(@Payload Order order,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
            @Header(KafkaHeaders.OFFSET) int offset) throws InterruptedException {
        log.info("Order received",
                StructuredArguments.kv("order", order),
                StructuredArguments.kv("partition", partition),
                StructuredArguments.kv("offset", offset));
        sleepService.sleep(50);
        log.info("Order processed",
                StructuredArguments.kv("order", order));
    }

}