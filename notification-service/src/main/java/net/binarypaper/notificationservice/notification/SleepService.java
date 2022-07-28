package net.binarypaper.notificationservice.notification;

import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.stereotype.Service;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Service
public class SleepService {

    private final Counter sleepCounter;

    public SleepService(MeterRegistry meterRegistry) {
        sleepCounter = Counter.builder("sleep.counter")
                .tag("mytag", "123")
                .description("A counter of how many times the sleep method was called")
                .register(meterRegistry);

    }

    @NewSpan
    @Timed(value = "sleep.method", extraTags = {"mytag", "456"})
    public void sleep(@SpanTag("milliSeconds") long milliSeconds) throws InterruptedException {
        sleepCounter.increment();
        Thread.sleep(milliSeconds);
    }
}