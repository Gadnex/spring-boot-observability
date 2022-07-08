package net.binarypaper.notificationservice.notification;

import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.stereotype.Service;

@Service
public class SleepService {
    
    @NewSpan
    public void sleep(@SpanTag("milliSeconds") long milliSeconds) throws InterruptedException {
        Thread.sleep(milliSeconds);
    }
}