package net.binarypaper.notificationservice.notification;

import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.observation.annotation.Observed;

@Service
@Observed(lowCardinalityKeyValues = {"observedLowCardinalityKeyValuesTag", "123"})
public class SleepService {

    private final Counter sleepCounter;

    public SleepService(MeterRegistry meterRegistry) {
        sleepCounter = Counter.builder("sleep.counter")
                .tag("sleepCounterTag", "456")
                .description("A counter of how many times the sleep method was called")
                .register(meterRegistry);

    }

    public void sleep(long milliSeconds) throws InterruptedException {
        sleepCounter.increment();
        Thread.sleep(milliSeconds);
    }
}